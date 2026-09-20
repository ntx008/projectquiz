package com.quiz.oop.multitasking;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * ============================================================================
 * [Lecture 6: Multitasking - Threads & Runnable]
 * ClockTask: Background Task ที่รันบน Thread แยก ทำหน้าที่อัปเดตเวลานาฬิกา
 * ============================================================================
 */
public class ClockTask implements Runnable {
    private volatile boolean running = true;
    private volatile String currentTimeString = "12:00:00";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void run() {
        while (running) {
            currentTimeString = LocalTime.now().format(formatter);
            try {
                // Sleep 1 วินาที (Multitasking sleep)
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stop() {
        this.running = false;
    }

    public String getCurrentTimeString() {
        return currentTimeString;
    }
}
