package com.quiz.oop.multitasking;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ============================================================================
 * [Lecture 6: Multitasking - Multithreading Management]
 * MultitaskManager: ผู้จัดการจัดการ Background Threads และงานพร้อมกัน
 * ============================================================================
 */
public class MultitaskManager {
    private final ExecutorService threadPool;
    private final ClockTask clockTask;

    public MultitaskManager() {
        // สร้าง Thread Pool สำหรับการทำงานแบบมัลติทาสก์
        this.threadPool = Executors.newCachedThreadPool();
        this.clockTask = new ClockTask();
        
        // เริ่มต้น Background Clock Thread
        this.threadPool.submit(clockTask);
    }

    public void executeBackgroundTask(Runnable task) {
        threadPool.submit(task);
    }

    public String getCurrentTime() {
        return clockTask.getCurrentTimeString();
    }

    public void shutdown() {
        clockTask.stop();
        threadPool.shutdownNow();
    }
}
