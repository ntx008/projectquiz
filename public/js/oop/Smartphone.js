/**
 * ============================================================================
 * [OOP - เสาหลักที่ 2: Inheritance & เสาหลักที่ 1: Encapsulation]
 * คลาส Smartphone สืบทอดมาจาก Device
 * ----------------------------------------------------------------------------
 * - Inheritance: ใช้คำสำคัญ `extends` สืบทอดคุณสมบัติของ Device (id, brand, model, powerButton)
 * - Encapsulation: ควบคุมการเข้าถึงข้อมูลแบตเตอรี่, หน้าจอ, พื้นที่เก็บข้อมูล (Storage)
 *   ไม่เปิดให้แก้ไขตัวแปรโดยตรง มี validation และ logic ควบคุมความสมบูรณ์ของข้อมูล
 * ============================================================================
 */

import { Device } from './Device.js';

export class Smartphone extends Device {
    // Private fields สำหรับสมาร์ตโฟน (Encapsulation)
    #batteryLevel;
    #isScreenOn;
    #storageCapacityGB;
    #usedStorageGB;
    #volumeLevel;
    #isCharging;

    constructor(brand, model, storageGB = 128) {
        // เรียก Constructor ของคลาสแม่ (Device) ผ่าน super()
        super(brand, model);

        this.#batteryLevel = 100; // แบตเตอรี่เต็ม 100% ตลอดเวลา
        this.#isScreenOn = false;
        this.#storageCapacityGB = storageGB;
        this.#usedStorageGB = 18.5; // ระบบปฏิบัติการใช้พื้นที่ไปบางส่วน
        this.#volumeLevel = 70;
        this.#isCharging = false;
    }

    // Getters
    get batteryLevel() { return this.#batteryLevel; }
    get isScreenOn() { return this.#isScreenOn; }
    get storageInfo() {
        return {
            total: this.#storageCapacityGB,
            used: parseFloat(this.#usedStorageGB.toFixed(2)),
            available: parseFloat((this.#storageCapacityGB - this.#usedStorageGB).toFixed(2))
        };
    }
    get volumeLevel() { return this.#volumeLevel; }
    get isCharging() { return this.#isCharging; }

    /**
     * ชาร์จแบตเตอรี่ (Encapsulated method ที่มี logic ตรวจสอบความถูกต้อง)
     */
    charge(amount = 15) {
        this.#batteryLevel = 100;
        return {
            success: true,
            batteryLevel: 100,
            message: `⚡ แบตเตอรี่เต็ม 100% ตลอดเวลา`
        };
    }

    /**
     * แบตเตอรี่ไม่มีวันลด (Infinite Battery)
     */
    drainBattery(amount = 5) {
        if (!this.isPoweredOn) {
            return { success: false, message: "เครื่องปิดอยู่ ไม่มีการใช้พลังงาน" };
        }

        // แบตเตอรี่ไม่มีวันลด คงที่ 100% ตลอดเวลา
        this.#batteryLevel = 100;

        return {
            success: true,
            batteryLevel: 100,
            autoShutdown: false,
            message: "🔋 แบตเตอรี่เต็ม 100% ตลอดเวลา (ไม่มีวันลด)"
        };
    }

    /**
     * สลับเปิด/ปิดหน้าจอ
     */
    toggleScreen(forceState = null) {
        if (!this.isPoweredOn) {
            return { success: false, message: "ไม่สามารถเปิดหน้าจอได้เนื่องจากเครื่องปิดอยู่" };
        }

        this.#isScreenOn = forceState !== null ? forceState : !this.#isScreenOn;
        return {
            success: true,
            isScreenOn: this.#isScreenOn,
            message: this.#isScreenOn ? "หน้าจอเปิดทำงาน" : "หน้าจอดับ (พักหน้าจอ)"
        };
    }

    /**
     * เพิ่ม/ลดระดับเสียง (0 - 100)
     */
    adjustVolume(delta) {
        const oldVolume = this.#volumeLevel;
        this.#volumeLevel = Math.max(0, Math.min(100, this.#volumeLevel + delta));
        return {
            success: true,
            volume: this.#volumeLevel,
            message: `🔊 ระดับเสียง: ${this.#volumeLevel}%`
        };
    }

    /**
     * จัดการพื้นที่จัดเก็บข้อมูล (Storage management internal method)
     */
    allocateStorage(sizeMB) {
        const sizeGB = sizeMB / 1024;
        if (this.#usedStorageGB + sizeGB > this.#storageCapacityGB) {
            return { success: false, message: "พื้นที่จัดเก็บข้อมูลไม่เพียงพอ!" };
        }
        this.#usedStorageGB += sizeGB;
        return { success: true, remainingGB: this.#storageCapacityGB - this.#usedStorageGB };
    }

    freeStorage(sizeMB) {
        const sizeGB = sizeMB / 1024;
        this.#usedStorageGB = Math.max(18.5, this.#usedStorageGB - sizeGB);
        return { success: true, remainingGB: this.#storageCapacityGB - this.#usedStorageGB };
    }
}
