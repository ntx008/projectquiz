package com.quiz.oop.event;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * [Lecture 5.1: Event-driven Programming]
 * EventDispatcher: ตัวกระจายเหตุการณ์ (Event Bus / Dispatcher)
 * จัดการลงทะเบียน Listener และส่งต่อเหตุการณ์ไปยังผู้รับ
 * ============================================================================
 */
public class EventDispatcher {
    private final List<PhoneEventListener> listeners = new ArrayList<>();

    public synchronized void addListener(PhoneEventListener listener) {
        listeners.add(listener);
    }

    public synchronized void removeListener(PhoneEventListener listener) {
        listeners.remove(listener);
    }

    public synchronized void dispatch(PhoneEvent event) {
        for (PhoneEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
