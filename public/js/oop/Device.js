/**
 * ============================================================================
 * [OOP - เสาหลักที่ 1 & 4: Abstraction & Encapsulation]
 * คลาสฐาน Device (Abstract Base Class)
 * ----------------------------------------------------------------------------
 * - Abstraction: กำหนดโครงร่างและสัญญาระดับสูง (Contracts) ไม่ยอมให้สร้างออบเจกต์ Device
 *   ตรงๆ (ต้องสืบทอดผ่านคลาสลูกเท่านั้น)
 * - Encapsulation: ซ่อนฟิลด์ข้อมูลสำคัญด้วย Private Fields (#) เช่น #serialNumber, #isPoweredOn
 * ============================================================================
 */

export class Device {
    // Private fields (Encapsulation)
    #id;
    #brand;
    #model;
    #serialNumber;
    #isPoweredOn;
    #createdAt;

    constructor(brand, model) {
        // ป้องกันไม่ให้ instantiate คลาส Device ตรงๆ (Abstraction)
        if (new.target === Device) {
            throw new Error("[OOP Abstraction Error] Device เป็น Abstract Class ไม่สามารถ new Device() ตรงๆ ได้ ต้องสืบทอดผ่านคลาสลูกเท่านั้น");
        }

        if (!brand || !model) {
            throw new Error("[OOP Error] ต้องระบุ brand และ model ของอุปกรณ์เสมอ");
        }

        this.#id = 'DEV-' + Math.random().toString(36).substr(2, 9).toUpperCase();
        this.#brand = brand;
        this.#model = model;
        this.#serialNumber = 'SN-' + Math.floor(10000000 + Math.random() * 90000000);
        this.#isPoweredOn = false;
        this.#createdAt = new Date();
    }

    // Getters (Encapsulation: เปิดให้อ่านค่าได้ แต่ห้ามแก้ไขโดยตรง)
    get id() { return this.#id; }
    get brand() { return this.#brand; }
    get model() { return this.#model; }
    get serialNumber() { return this.#serialNumber; }
    get isPoweredOn() { return this.#isPoweredOn; }
    get createdAt() { return this.#createdAt; }

    /**
     * สลับสถานะเปิด/ปิดเครื่อง (Power Button)
     */
    powerButton() {
        this.#isPoweredOn = !this.#isPoweredOn;
        const status = this.#isPoweredOn ? "เปิดเครื่อง (Powered ON)" : "ปิดเครื่อง (Powered OFF)";
        return {
            success: true,
            isPoweredOn: this.#isPoweredOn,
            message: `[Device: ${this.#brand} ${this.#model}] ${status}`
        };
    }

    /**
     * Abstract Method: ต้องถูก Override ในคลาสลูกเสมอ (Abstraction)
     */
    getDeviceInfo() {
        throw new Error("[OOP Abstraction Error] เมธอด getDeviceInfo() เป็น Abstract Method ต้องถูก Implement ในคลาสลูก");
    }

    /**
     * Abstract Method: บูตระบบ
     */
    bootSequence() {
        throw new Error("[OOP Abstraction Error] เมธอด bootSequence() ต้องถูก Implement ในคลาสลูก");
    }
}
