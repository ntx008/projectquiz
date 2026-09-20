package com.quiz.oop.model.app;

/**
 * ============================================================================
 * [Lecture 4: Polymorphism]
 * SettingsApp: หน้าจอตั้งค่าระบบแสดงข้อมูล OOP และสเปกเครื่อง
 * ============================================================================
 */
public class SettingsApp extends App {
    public SettingsApp() {
        super("ตั้งค่า (Settings)", "com.android.settings", "⚙️", 18);
    }

    @Override
    public String renderContent() {
        return """
            <div class='settings-container'>
                <h3 style='margin-bottom: 12px;'>⚙️ การตั้งค่าระบบ (System Settings)</h3>
                
                <div class='setting-group'>
                    <div class='setting-group-title'>ข้อมูลอุปกรณ์ (Device Status)</div>
                    <div class='setting-row'><span>🔋 แบตเตอรี่คงเหลือ:</span> <strong>100% (Infinite Battery)</strong></div>
                    <div class='setting-row'><span>💾 หน่วยความจำ:</span> <strong>24.5 / 512 GB</strong></div>
                    <div class='setting-row'><span>🔊 ระดับเสียง:</span> <strong>80%</strong></div>
                    <div class='setting-row'><span>🔓 ความปลอดภัย:</span> <strong>ไม่ตั้งรหัสผ่าน (Swipe / None)</strong></div>
                </div>

                <div class='setting-group' style='margin-top: 14px;'>
                    <div class='setting-group-title'>ความเชื่อมโยงกับบทเรียน (Course Syllabus Alignment)</div>
                    <div class='setting-row'><span>Lecture 2:</span> <strong>Coding in Java (100% Pure Java Backend)</strong></div>
                    <div class='setting-row'><span>Lecture 3:</span> <strong>OOP Class and Object (Device, Smartphone, App)</strong></div>
                    <div class='setting-row'><span>Lecture 4:</span> <strong>OOP Concepts (Encapsulation, Inheritance, Poly)</strong></div>
                    <div class='setting-row'><span>Lecture 5.1:</span> <strong>Event-driven (PhoneEventListener & Bus)</strong></div>
                    <div class='setting-row'><span>Lecture 5.2:</span> <strong>MVC Pattern (Model-View-Controller)</strong></div>
                    <div class='setting-row'><span>Lecture 6:</span> <strong>Multitasking (Background Clock & Threads)</strong></div>
                </div>
            </div>
            """;
    }

    @Override
    public String performAction(String actionParam) {
        return "เปิดดูการตั้งค่าระบบเรียบร้อย";
    }
}
