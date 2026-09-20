package com.quiz.oop;

import com.quiz.oop.controller.PhoneController;
import com.quiz.oop.event.EventDispatcher;
import com.quiz.oop.model.AndroidPhone;
import com.quiz.oop.multitasking.MultitaskManager;
import com.quiz.oop.server.PhoneHttpServer;

/**
 * ============================================================================
 * [Lecture 0 - 6: Java OOP Android Phone Project]
 * Main Entry Point: จุดเริ่มต้นการทำงานของโปรแกรมทั้งหมดในภาษา Java
 * ----------------------------------------------------------------------------
 * ครอบคลุมเนื้อหาทั้ง 6 บทเรียน:
 * - Lecture 2: Coding in Java (Java 21 Syntax & Packages)
 * - Lecture 3: OOP Class and Object (Instantiating AndroidPhone, Apps)
 * - Lecture 4: OOP Concept (Encapsulation, Inheritance, Polymorphism, Abstraction)
 * - Lecture 5.1: Event-driven Programming (EventDispatcher, PhoneEventListener)
 * - Lecture 5.2: MVC Architectural Pattern (Model, View, Controller)
 * - Lecture 6: Multitasking (MultitaskManager, Background Clock Thread)
 * ============================================================================
 */
public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Starting Java OOP Android Simulator...");

            // 1. สร้าง Model (Lecture 3 & 4)
            AndroidPhone phoneModel = new AndroidPhone();
            System.out.println("[Model Initialized]: " + phoneModel.getDeviceInfo());

            // 2. สร้าง Event Dispatcher สำหรับ Event-driven programming (Lecture 5.1)
            EventDispatcher eventDispatcher = new EventDispatcher();

            // 3. สร้าง Controller (Lecture 5.2 MVC Pattern)
            PhoneController controller = new PhoneController(phoneModel, eventDispatcher);

            // 4. สร้าง Multitask Manager สำหรับงานพื้นหลัง (Lecture 6 Multitasking)
            MultitaskManager multitaskManager = new MultitaskManager();

            // 5. เริ่มต้น HTTP Server สำหรับแสดงผล UI บน Localhost:3000
            int port = 3000;
            PhoneHttpServer server = new PhoneHttpServer(port, controller, multitaskManager);
            server.start();

            // เพิ่ม Shutdown Hook สำหรับปิดระบบเมื่อกด Ctrl+C
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutting down Java OOP Simulator...");
                multitaskManager.shutdown();
                server.stop();
            }));

        } catch (Exception e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
