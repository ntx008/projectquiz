package com.quiz.oop.event;

/**
 * ============================================================================
 * [Lecture 5.1: Event-driven Programming]
 * Interface PhoneEventListener: Callback Interface สำหรับตัวรับเหตุการณ์ (Listener)
 * ============================================================================
 */
@FunctionalInterface
public interface PhoneEventListener {
    void onEvent(PhoneEvent event);
}
