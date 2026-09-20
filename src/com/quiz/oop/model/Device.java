package com.quiz.oop.model;

import java.util.UUID;

/**
 * ============================================================================
 * [Lecture 3: OOP Class and Object & Lecture 4: OOP Concept - Abstraction]
 * Abstract Class Device: คลาสฐานระดับสูงสำหรับอุปกรณ์อิเล็กทรอนิกส์ทั้งหมด
 * ----------------------------------------------------------------------------
 * - ไม่สามารถ new Device() ตรงๆ ได้ (Abstraction)
 * - ซ่อนข้อมูลสำคัญด้วย private และเปิดให้เข้าถึงผ่าน Getter (Encapsulation)
 * ============================================================================
 */
public abstract class Device implements Powerable {
    // Encapsulation: Private Fields (Lecture 4)
    private final String id;
    private final String brand;
    private final String model;
    private final String serialNumber;
    private boolean isPoweredOn;

    // Constructor (Lecture 3)
    public Device(String brand, String model) {
        this.id = "DEV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.brand = brand;
        this.model = model;
        this.serialNumber = "SN-" + (int)(Math.random() * 90000000 + 10000000);
        this.isPoweredOn = true; // เปิดเครื่องเริ่มต้น
    }

    // Getters (Encapsulation)
    public String getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public String getSerialNumber() { return serialNumber; }

    @Override
    public boolean isPoweredOn() { return isPoweredOn; }

    @Override
    public void powerButton() {
        this.isPoweredOn = !this.isPoweredOn;
    }

    // Abstract Methods (Lecture 4: Abstraction - ต้องถูก Override ในคลาสลูก)
    public abstract String bootSequence();
    public abstract String getDeviceInfo();
}
