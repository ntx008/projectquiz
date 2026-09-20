package com.quiz.oop.server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.quiz.oop.controller.PhoneController;
import com.quiz.oop.multitasking.MultitaskManager;
import com.quiz.oop.view.PhoneWebView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================================
 * [Pure Java HTTP Server]
 * PhoneHttpServer: เซิร์ฟเวอร์เว็บในตัว (Embedded) ด้วย Standard Java Library
 * ให้บริการแสดงผลโทรศัพท์ Android บน http://localhost:3000
 * ============================================================================
 */
public class PhoneHttpServer {
    private final int port;
    private final PhoneController controller;
    private final MultitaskManager multitaskManager;
    private HttpServer server;

    public PhoneHttpServer(int port, PhoneController controller, MultitaskManager multitaskManager) {
        this.port = port;
        this.controller = controller;
        this.multitaskManager = multitaskManager;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        // Route 1: หน้าเว็บหลักแสดงมือถือ Android (View)
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    String path = exchange.getRequestURI().getPath();
                    if (!path.equals("/") && !path.equals("/index.html")) {
                        exchange.sendResponseHeaders(404, -1);
                        return;
                    }

                    String htmlResponse = PhoneWebView.renderPage(
                            controller.getModel(),
                            multitaskManager.getCurrentTime()
                    );

                    byte[] bytes = htmlResponse.getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                    exchange.sendResponseHeaders(200, bytes.length);

                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(bytes);
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                    String err = "500 Internal Error: " + t.getMessage();
                    byte[] bytes = err.getBytes(StandardCharsets.UTF_8);
                    exchange.sendResponseHeaders(500, bytes.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(bytes);
                    }
                }
            }
        });

        // Route 2: API Action Endpoint (Controller)
        server.createContext("/api/action", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                try {
                    Map<String, String> queryParams = parseQuery(exchange.getRequestURI().getQuery());
                    String actionType = queryParams.get("type");
                    String param = queryParams.getOrDefault("param", "");

                    // ส่งการกระทำไปยัง MVC Controller (Lecture 5.2)
                    controller.handleAction(actionType, param);

                    String jsonResponse = "{\"status\":\"success\", \"reload\":true}";
                    byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                    exchange.sendResponseHeaders(200, bytes.length);

                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(bytes);
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                    String err = "{\"status\":\"error\", \"message\":\"" + t.getMessage() + "\"}";
                    byte[] bytes = err.getBytes(StandardCharsets.UTF_8);
                    exchange.sendResponseHeaders(500, bytes.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(bytes);
                    }
                }
            }
        });

        server.setExecutor(null);
        server.start();

        System.out.println("==================================================================");
        System.out.println("🤖 Android Smartphone OOP Simulator (100% Pure Java)");
        System.out.println("🌐 เปิดดูในเบราว์เซอร์ได้ที่: http://localhost:" + port);
        System.out.println("==================================================================");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> result = new HashMap<>();
        if (query == null || query.isBlank()) return result;

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                result.put(
                    URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                    URLDecoder.decode(kv[1], StandardCharsets.UTF_8)
                );
            }
        }
        return result;
    }
}
