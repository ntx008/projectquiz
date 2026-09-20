package com.quiz.oop.model.app;

/**
 * ============================================================================
 * [Lecture 3 & 4: OOP Class, Object & Polymorphism]
 * Base Class App: แม่แบบสำหรับแอปพลิเคชันบนสมาร์ตโฟน
 * ============================================================================
 */
public abstract class App {
    private final String name;
    private final String packageName;
    private final String icon;
    private final int sizeMB;

    public App(String name, String packageName, String icon, int sizeMB) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
        this.sizeMB = sizeMB;
    }

    public String getName() { return name; }
    public String getPackageName() { return packageName; }
    public String getIcon() { return icon; }
    public int getSizeMB() { return sizeMB; }

    // Polymorphic method to be implemented by each app
    public abstract String renderContent();
    public abstract String performAction(String actionParam);
}
