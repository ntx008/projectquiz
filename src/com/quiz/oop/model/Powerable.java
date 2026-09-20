package com.quiz.oop.model;

/**
 * ============================================================================
 * [Lecture 4: OOP Concept - Abstraction & Interface]
 * Interface Powerable: กำหนดสัญญา (Contract) สำหรับอุปกรณ์ที่สามารถเปิด/ปิดได้
 * ============================================================================
 */
public interface Powerable {
    boolean isPoweredOn();
    void powerButton();
}
