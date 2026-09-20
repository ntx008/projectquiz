package com.quiz.oop.event;

import java.time.LocalDateTime;

/**
 * ============================================================================
 * [Lecture 5.1: Event-driven Programming]
 * PhoneEvent: คลาสตัวแทนเหตุการณ์ (Event Object) ที่เกิดขึ้นในระบบ
 * ============================================================================
 */
public class PhoneEvent {
    private final String eventType;
    private final String payload;
    private final LocalDateTime timestamp;

    public PhoneEvent(String eventType, String payload) {
        this.eventType = eventType;
        this.payload = payload;
        this.timestamp = LocalDateTime.now();
    }

    public String getEventType() { return eventType; }
    public String getPayload() { return payload; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("[%s] Event: %s, Payload: %s", timestamp, eventType, payload);
    }
}
