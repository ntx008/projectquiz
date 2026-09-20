package com.quiz.oop.model;

/**
 * ============================================================================
 * [Lecture 4: OOP Concept - Inheritance & Encapsulation]
 * Class Smartphone: สืบทอดคุณสมบัติมาจาก Device ด้วยคำสำคัญ 'extends'
 * ----------------------------------------------------------------------------
 * - Encapsulation: ซ่อนตัวแปร batteryLevel, volumeLevel ไว้ภายใน
 * - ตามข้อกำหนดของผู้ใช้: "แบตไม่มีวันลด" -> batteryLevel จะมีค่า 100% ตลอดเวลา
 * ============================================================================
 */
public class Smartphone extends Device {
    // Encapsulation: Private fields
    private int batteryLevel; // ค่าแบตเตอรี่ (%)
    private int volumeLevel;  // ระดับเสียง (0 - 100)
    private final int storageCapacityGB;
    private double usedStorageGB;

    public Smartphone(String brand, String model, int storageCapacityGB) {
        super(brand, model); // เรียก Constructor ของคลาสแม่ (Device)
        this.batteryLevel = 100; // แบตเตอรี่เต็ม 100% ตลอดเวลา
        this.volumeLevel = 80;
        this.storageCapacityGB = storageCapacityGB;
        this.usedStorageGB = 24.5;
    }

    // Getters & Setters (Encapsulation)
    public int getBatteryLevel() {
        return this.batteryLevel;
    }

    public int getVolumeLevel() {
        return this.volumeLevel;
    }

    public int getStorageCapacityGB() {
        return this.storageCapacityGB;
    }

    public double getUsedStorageGB() {
        return this.usedStorageGB;
    }

    /**
     * เมธอดปรับระดับเสียง (พร้อมระบบ Validation)
     */
    public void adjustVolume(int delta) {
        this.volumeLevel = Math.max(0, Math.min(100, this.volumeLevel + delta));
    }

    /**
     * การชาร์จแบตเตอรี่
     */
    public void charge(int amount) {
        this.batteryLevel = 100;
    }

    /**
     * [ข้อกำหนดพิเศษ]: แบตเตอรี่ไม่มีวันลด (Infinite Battery)
     * ไม่ว่าจะเรียกใช้งานฟังก์ชันใด แบตเตอรี่จะคงที่อยู่ที่ 100% เสมอ
     */
    public void drainBattery(int amount) {
        this.batteryLevel = 100;
    }

    @Override
    public String bootSequence() {
        return "Smart Device OS Booting...";
    }

    @Override
    public String getDeviceInfo() {
        return String.format("%s %s (Storage: %.1f/%d GB, Battery: %d%%)",
                getBrand(), getModel(), usedStorageGB, storageCapacityGB, batteryLevel);
    }
}
