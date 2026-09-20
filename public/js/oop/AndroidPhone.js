/**
 * ============================================================================
 * [OOP - เสาหลักที่ 2 & 3: Inheritance & Polymorphism]
 * คลาส AndroidPhone สืบทอดมาจาก Smartphone
 * ----------------------------------------------------------------------------
 * - สืบทอดฮาร์ดแวร์พื้นฐานและแบตเตอรี่มาจาก Smartphone
 * - เพิ่มสถาปัตยกรรมเฉพาะของ Android OS: Package Manager (การจัดการแอป),
 *   Notification Manager, Window Manager (เปิด/ปิดแอป), และระบบความปลอดภัย PIN
 * ============================================================================
 */

import { Smartphone } from './Smartphone.js';
import { CameraApp, MusicPlayerApp, SettingsApp, PlayStoreApp, NotesApp } from './App.js';

export class AndroidPhone extends Smartphone {
    // Private fields สำหรับระบบ Android
    #androidVersion;
    #apiLevel;
    #installedApps;
    #notifications;
    #isLocked;
    #pinCode;
    #currentApp;
    #recentApps;
    #accentColor;

    constructor(brand, model, storageGB = 256, androidVer = "Android 15") {
        super(brand, model, storageGB);

        this.#androidVersion = androidVer;
        this.#apiLevel = 35;
        this.#installedApps = new Map();
        this.#notifications = [];
        this.#isLocked = false; // ระบบไม่ล็อกหน้าจอ (ไม่ต้องใส่รหัส)
        this.#pinCode = "";
        this.#currentApp = null;
        this.#recentApps = [];
        this.#accentColor = "#3DDC84"; // Android Green

        // ติดตั้ง System Apps เริ่มต้น
        this.#installDefaultSystemApps();
    }

    // Getters
    get androidVersion() { return this.#androidVersion; }
    get apiLevel() { return this.#apiLevel; }
    get isLocked() { return this.#isLocked; }
    get currentApp() { return this.#currentApp; }
    get accentColor() { return this.#accentColor; }
    get notifications() { return [...this.#notifications]; }
    get installedAppsList() {
        return Array.from(this.#installedApps.values());
    }

    /**
     * ติดตั้งแอปพลิเคชันระบบเริ่มต้น
     */
    #installDefaultSystemApps() {
        const defaultApps = [
            new CameraApp(),
            new MusicPlayerApp(),
            new SettingsApp(),
            new PlayStoreApp(),
            new NotesApp()
        ];
        defaultApps.forEach(app => {
            this.#installedApps.set(app.packageName, app);
        });
    }

    /**
     * ปลดล็อกหน้าจอโทรศัพท์ (ตรวจสอบ PIN - Encapsulation)
     */
    unlock(enteredPin) {
        if (!this.isPoweredOn) {
            return { success: false, message: "เครื่องยังไม่ได้เปิดทำงาน" };
        }

        if (enteredPin === this.#pinCode) {
            this.#isLocked = false;
            return {
                success: true,
                message: "🔓 ปลดล็อกเครื่องสำเร็จ ยินดีต้อนรับสู่หน้าจอหลัก"
            };
        } else {
            return {
                success: false,
                message: "❌ รหัส PIN ไม่ถูกต้อง (รหัสเริ่มต้นคือ 1234)"
            };
        }
    }

    /**
     * ล็อกหน้าจอ
     */
    lock() {
        this.#isLocked = true;
        this.#currentApp = null;
        return { success: true, message: "🔒 ล็อกหน้าจอเรียบร้อยแล้ว" };
    }

    /**
     * ติดตั้งแอปพลิเคชันใหม่ (Dynamic Object Injection)
     */
    installApp(appInstance) {
        if (this.#installedApps.has(appInstance.packageName)) {
            return { success: false, message: `แอป ${appInstance.name} ติดตั้งอยู่แล้ว` };
        }

        // จัดสรรพื้นที่จัดเก็บ
        const allocResult = this.allocateStorage(appInstance.sizeMB);
        if (!allocResult.success) {
            return { success: false, message: allocResult.message };
        }

        this.#installedApps.set(appInstance.packageName, appInstance);
        this.pushNotification("Google Play", `ติดตั้งแอป ${appInstance.name} เรียบร้อยแล้ว`, "📥");

        return {
            success: true,
            app: appInstance,
            message: `✅ ติดตั้ง ${appInstance.name} สำเร็จ (ขนาด ${appInstance.sizeMB} MB)`
        };
    }

    /**
     * ถอนการติดตั้งแอป
     */
    uninstallApp(packageName) {
        const app = this.#installedApps.get(packageName);
        if (!app) {
            return { success: false, message: "ไม่พบแอปที่ต้องการถอนการติดตั้ง" };
        }

        if (app.isSystemApp) {
            return { success: false, message: "⚠️ ไม่สามารถถอนการติดตั้งแอปพลิเคชันของระบบได้" };
        }

        this.freeStorage(app.sizeMB);
        this.#installedApps.delete(packageName);

        if (this.#currentApp && this.#currentApp.packageName === packageName) {
            this.#currentApp = null;
        }

        return {
            success: true,
            message: `🗑️ ถอนการติดตั้ง ${app.name} เรียบร้อยแล้ว`
        };
    }

    /**
     * เปิดแอปพลิเคชัน (Launch App)
     */
    launchApp(packageName) {
        if (!this.isPoweredOn) {
            return { success: false, message: "เครื่องปิดอยู่" };
        }
        if (this.#isLocked) {
            return { success: false, message: "กรุณาปลดล็อกหน้าจอก่อนเปิดแอป" };
        }

        const app = this.#installedApps.get(packageName);
        if (!app) {
            return { success: false, message: `ไม่พบแอปที่มี Package Name: ${packageName}` };
        }

        this.#currentApp = app;
        if (!this.#recentApps.includes(packageName)) {
            this.#recentApps.unshift(packageName);
        }

        // ลดแบตเตอรี่เล็กน้อยเมื่อใช้งาน
        this.drainBattery(2);

        return {
            success: true,
            app: app,
            message: `🚀 เปิดแอป: ${app.name}`
        };
    }

    /**
     * ปิดแอปปัจจุบัน (กลับสู่หน้าจอโฮม)
     */
    closeCurrentApp() {
        if (this.#currentApp) {
            const closedName = this.#currentApp.name;
            this.#currentApp = null;
            return { success: true, message: `ปิดแอป ${closedName} กลับสู่หน้าจอโฮม` };
        }
        return { success: true, message: "อยู่ที่หน้าจอโฮมอยู่แล้ว" };
    }

    /**
     * ส่ง Notification แจ้งเตือนเข้าเครื่อง
     */
    pushNotification(title, message, icon = "🔔") {
        const notif = {
            id: Date.now() + Math.random(),
            title,
            message,
            icon,
            time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
        };
        this.#notifications.unshift(notif);

        // เก็บสูงสุด 10 รายการ
        if (this.#notifications.length > 10) {
            this.#notifications.pop();
        }

        return notif;
    }

    /**
     * ล้างการแจ้งเตือนทั้งหมด
     */
    clearNotifications() {
        this.#notifications = [];
        return { success: true, message: "ล้างการแจ้งเตือนทั้งหมดแล้ว" };
    }

    /**
     * ข้อมูลลำดับขั้นการสืบทอด (OOP Class Hierarchy Inspector)
     */
    getHierarchyChain() {
        let chain = [];
        let proto = Object.getPrototypeOf(this);
        while (proto && proto.constructor && proto.constructor.name !== "Object") {
            chain.unshift(proto.constructor.name);
            proto = Object.getPrototypeOf(proto);
        }
        chain.unshift("Object");
        return chain;
    }

    /**
     * เมธอดพื้นฐานของ Android (ที่จะถูก Polymorphic Override ในแบรนด์ต่างๆ)
     */
    bootSequence() {
        return {
            bootTitle: "Android",
            subText: "Powered by Android",
            accentColor: "#3DDC84",
            sound: "generic-boot.mp3"
        };
    }

    takePhoto() {
        return {
            brand: this.brand,
            resolution: "50MP Standard Lens",
            filter: "Standard Android RGB",
            postProcessing: "Android Camera2 API Pipeline",
            photoUrl: "assets/photos/standard.jpg"
        };
    }

    triggerAssistant() {
        return {
            assistantName: "Google Assistant",
            greeting: "สวัสดีครับ มีอะไรให้ช่วยไหม?",
            color: "#4285F4"
        };
    }

    getDeviceInfo() {
        return {
            id: this.id,
            brand: this.brand,
            model: this.model,
            serialNumber: this.serialNumber,
            os: this.#androidVersion,
            battery: `${this.batteryLevel}%`,
            storage: `${this.storageInfo.used}/${this.storageInfo.total} GB`,
            appsCount: this.#installedApps.size
        };
    }
}
