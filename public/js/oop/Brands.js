/**
 * ============================================================================
 * [OOP - เสาหลักที่ 3: Polymorphism (ภาวะหลายรูปแบบ)]
 * คลาสเฉพาะแบรนด์: SamsungGalaxy, GooglePixel, XiaomiPhone
 * ----------------------------------------------------------------------------
 * - ทุกคลาสสืบทอดมาจาก `AndroidPhone`
 * - แต่ละคลาสทำการ Override เมธอดเดียวกัน (เช่น takePhoto, bootSequence, triggerAssistant)
 *   แต่มีพฤติกรรมและการคำนวณที่แตกต่างกันตามคุณลักษณะเฉพาะของแต่ละแบรนด์!
 * ============================================================================
 */

import { AndroidPhone } from './AndroidPhone.js';

/**
 * 📱 1. Samsung Galaxy (ซัมซุง กาแลคซี่)
 */
export class SamsungGalaxy extends AndroidPhone {
    #oneUiVersion;
    #hasSPen;
    #knoxSecurityStatus;

    constructor(model = "Galaxy S24 Ultra", storageGB = 512) {
        super("Samsung", model, storageGB, "Android 15 (One UI 6.1.1)");
        this.#oneUiVersion = "One UI 6.1.1";
        this.#hasSPen = true;
        this.#knoxSecurityStatus = "Knox Vault 3.0 Active";
    }

    get oneUiVersion() { return this.#oneUiVersion; }
    get hasSPen() { return this.#hasSPen; }

    // [Polymorphism] Override bootSequence เฉพาะของ Samsung
    bootSequence() {
        return {
            brandName: "SAMSUNG",
            bootTitle: "Samsung Galaxy",
            subText: "Secured by Knox | Powered by Android",
            accentColor: "#034EA2",
            animationType: "samsung-glow"
        };
    }

    // [Polymorphism] Override takePhoto ด้วยระบบกล้องและ AI ของ Samsung
    takePhoto() {
        // หักพลังงาน
        this.drainBattery(3);
        return {
            brand: "Samsung Galaxy",
            feature: "🔭 100x Space Zoom & ProVisual Engine",
            resolution: "200 MP Quad Telephoto System",
            sensor: "ISOCELL HP2SX Sensor",
            aiProcessing: "Galaxy AI Object-aware engine ปรับแต่งสีสันให้คมชัดระดับ 8K",
            imageTag: "samsung_shot",
            watermark: "Captured on Galaxy S24 Ultra"
        };
    }

    // [Polymorphism] Override triggerAssistant เป็น Galaxy AI / Bixby
    triggerAssistant() {
        return {
            assistantName: "Galaxy AI (Bixby)",
            greeting: "✨ สวัสดีค่ะ Galaxy AI พร้อมช่วยแปลภาษาแบบเรียลไทม์ และแต่งรูปแล้วค่ะ",
            badge: "Live Translate & Note Assist",
            color: "#1428A0"
        };
    }

    // ฟังก์ชันเฉพาะตัวของ Samsung
    activateSPen() {
        return {
            success: true,
            feature: "🖊️ S-Pen Air Command",
            message: "ดึงปากกา S-Pen ออกมาแล้ว พร้อมเขียนโน้ตทันทีบนหน้าจอล็อก!"
        };
    }
}

/**
 * 📱 2. Google Pixel (กูเกิล พิกเซล)
 */
export class GooglePixel extends AndroidPhone {
    #tensorChip;
    #materialYouTheme;

    constructor(model = "Pixel 9 Pro", storageGB = 256) {
        super("Google", model, storageGB, "Android 15 (Pixel Experience)");
        this.#tensorChip = "Google Tensor G4";
        this.#materialYouTheme = "Dynamic Material You (Amber Rose)";
    }

    get tensorChip() { return this.#tensorChip; }

    // [Polymorphism] Override bootSequence เฉพาะของ Google
    bootSequence() {
        return {
            brandName: "Google",
            bootTitle: "Google",
            subText: "#MadeByGoogle",
            accentColor: "#4285F4",
            animationType: "google-pulse"
        };
    }

    // [Polymorphism] Override takePhoto ด้วย Computational Photography ของ Google
    takePhoto() {
        this.drainBattery(2);
        return {
            brand: "Google Pixel",
            feature: "✨ Magic Editor & Real Tone Night Sight",
            resolution: "50 MP Octa PD Octa Quad Bayer",
            sensor: "Custom Tensor ISP Computational Photography",
            aiProcessing: "ประมวลผลแสง HDR+ และ Real Tone ให้สีผิวธรรมชาติแม้ในที่มืดสนิท",
            imageTag: "pixel_shot",
            watermark: "Shot on Pixel 9 Pro"
        };
    }

    // [Polymorphism] Override triggerAssistant เป็น Google Gemini Nano
    triggerAssistant() {
        return {
            assistantName: "Google Gemini Nano",
            greeting: "🤖 สวัสดี! ฉันคือ Gemini ผู้ช่วย AI จาก Google ที่รัน On-Device โดยตรง",
            badge: "Gemini Nano On-Device AI",
            color: "#EA4335"
        };
    }

    // ฟังก์ชันเฉพาะตัวของ Pixel
    magicEraser() {
        return {
            success: true,
            feature: "🪄 Magic Eraser",
            message: "ลบคนและวัตถุที่ไม่ต้องการออกจากภาพด้วยพลัง Google AI Tensor"
        };
    }
}

/**
 * 📱 3. Xiaomi (เสียวหมี่)
 */
export class XiaomiPhone extends AndroidPhone {
    #hyperOsVersion;
    #chargingWattage;

    constructor(model = "14 Ultra", storageGB = 512) {
        super("Xiaomi", model, storageGB, "Android 15 (Xiaomi HyperOS 2.0)");
        this.#hyperOsVersion = "HyperOS 2.0";
        this.#chargingWattage = 120; // 120W HyperCharge
    }

    get hyperOsVersion() { return this.#hyperOsVersion; }
    get chargingWattage() { return this.#chargingWattage; }

    // [Polymorphism] Override bootSequence เฉพาะของ Xiaomi HyperOS
    bootSequence() {
        return {
            brandName: "Xiaomi",
            bootTitle: "Xiaomi HyperOS",
            subText: "Human x Car x Home Ecosystem",
            accentColor: "#FF6900",
            animationType: "xiaomi-flare"
        };
    }

    // [Polymorphism] Override takePhoto ด้วยเลนส์และฟิลเตอร์สี Leica
    takePhoto() {
        this.drainBattery(2);
        return {
            brand: "Xiaomi",
            feature: "🔴 Leica Summilux 1-inch Master Optics",
            resolution: "50 MP 1-inch LYT-900 Sensor (Stepless Variable Aperture)",
            sensor: "Leica Vario-Summilux 1:1.63-2.5/12-120 ASPH.",
            aiProcessing: "Leica Authentic Look ให้คอนทราสต์ลึกซึ้งและโบเก้ธรรมชาติระดับกล้องโปร",
            imageTag: "xiaomi_shot",
            watermark: "Leica Authentic | Xiaomi 14 Ultra"
        };
    }

    // [Polymorphism] Override เมธอด charge ของ Smartphone ให้ชาร์จเร็วกว่าปกติถึง 3 เท่า!
    charge(amount = 15) {
        // Xiaomi 120W HyperCharge ชาร์จเร็วกว่าปกติ 2 เท่า!
        const boostedAmount = Math.round(amount * 2.2);
        const result = super.charge(boostedAmount);
        result.message = `⚡⚡ [120W HyperCharge] ชาร์จด่วนพิเศษเพิ่มขึ้น +${boostedAmount}% (ปัจจุบัน: ${result.batteryLevel}%)`;
        return result;
    }

    // [Polymorphism] Override triggerAssistant เป็น Mi AI
    triggerAssistant() {
        return {
            assistantName: "Mi AI Assistant",
            greeting: "🏮 ยินดีต้อนรับสู่ระบบนิเวศ HyperOS เชื่อมต่อบ้านอัจฉริยะของคุณได้ทันที",
            badge: "HyperOS Ecosystem Connect",
            color: "#FF6900"
        };
    }
}
