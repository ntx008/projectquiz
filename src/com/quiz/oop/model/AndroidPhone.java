package com.quiz.oop.model;

import com.quiz.oop.model.app.*;
import java.util.*;

/**
 * ============================================================================
 * [Lecture 3 & 4: OOP Class, Object, Inheritance & Polymorphism]
 * Class AndroidPhone: สืบทอดมาจาก Smartphone
 * ----------------------------------------------------------------------------
 * - Inheritance: extends Smartphone -> extends Device
 * - Encapsulation: ซ่อน installedApps, currentApp, notifications ไว้ภายใน
 * - Polymorphism: Override bootSequence() และ getDeviceInfo()
 * - Single Phone: มือถือ Android เพียงเครื่องเดียว ไม่ต้องมีให้เลือก
 * - แบตเตอรี่ไม่มีวันลด: สืบทอดคุณสมบัติแบต 100% มาจาก Smartphone
 * ============================================================================
 */
public class AndroidPhone extends Smartphone {
    private final String androidVersion;
    private final Map<String, App> installedApps;
    private final List<String> notifications;
    private App currentApp; // แอปที่เปิดใช้งานอยู่ ณ ขณะนี้ (null = อยู่ที่ Home Screen)

    public AndroidPhone() {
        super("Android", "Galaxy Ultra Edition", 512);
        this.androidVersion = "Android 15 (Vanilla Ice Cream)";
        this.installedApps = new LinkedHashMap<>();
        this.notifications = new ArrayList<>();
        this.currentApp = null;

        // ติดตั้ง Default Apps
        installDefaultApps();
        
        // เปิดแอปเกมตอบปัญหาเชาว์ปัญญาเป็นแอปหลักเริ่มต้น
        this.currentApp = installedApps.get("com.cpe.quiz");

        // เพิ่มการแจ้งเตือนเริ่มต้น
        notifications.add("ยินดีต้อนรับสู่แอป 'เกมตอบปัญหาเชาว์ปัญญา' (CPE Portal)");
        notifications.add("ระบบทำงานด้วยสถาปัตยกรรม MVC และ Multitasking");
    }

    private void installDefaultApps() {
        App quiz = new QuizApp();
        App camera = new CameraApp();
        App music = new MusicApp();
        App settings = new SettingsApp();
        App notes = new NotesApp();

        installedApps.put(quiz.getPackageName(), quiz);
        installedApps.put(camera.getPackageName(), camera);
        installedApps.put(music.getPackageName(), music);
        installedApps.put(settings.getPackageName(), settings);
        installedApps.put(notes.getPackageName(), notes);
    }

    public String getAndroidVersion() { return androidVersion; }
    public Collection<App> getInstalledApps() { return installedApps.values(); }
    public App getCurrentApp() { return currentApp; }
    public List<String> getNotifications() { return Collections.unmodifiableList(notifications); }

    /**
     * เปิดแอปพลิเคชัน
     */
    public boolean launchApp(String packageName) {
        if (!isPoweredOn()) return false;
        App app = installedApps.get(packageName);
        if (app != null) {
            this.currentApp = app;
            // เรียก drainBattery (ซึ่งถูกกำหนดให้ไม่มีวันลด คงที่ 100% เสมอ)
            drainBattery(2);
            return true;
        }
        return false;
    }

    /**
     * ปิดแอปปัจจุบัน กลับสู่หน้าจอโฮม
     */
    public void closeApp() {
        this.currentApp = null;
    }

    /**
     * เพิ่มการแจ้งเตือนใหม่
     */
    public void addNotification(String msg) {
        notifications.add(0, msg);
        if (notifications.size() > 10) {
            notifications.remove(notifications.size() - 1);
        }
    }

    public void clearNotifications() {
        notifications.clear();
    }

    @Override
    public String bootSequence() {
        return "🤖 Android 15 Secured Boot | Powered by Java OOP";
    }

    @Override
    public String getDeviceInfo() {
        return String.format("%s %s (OS: %s, Battery: %d%% [Infinite], Apps: %d)",
                getBrand(), getModel(), androidVersion, getBatteryLevel(), installedApps.size());
    }
}
