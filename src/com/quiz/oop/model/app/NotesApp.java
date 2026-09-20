package com.quiz.oop.model.app;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * [Lecture 3 & 4: OOP Class, Object & Collections in Java]
 * NotesApp: แอปพลิเคชันสมุดบันทึกสรุปบทเรียน
 * ============================================================================
 */
public class NotesApp extends App {
    private final List<String> notes;

    public NotesApp() {
        super("บันทึก (Notes)", "com.android.notes", "📝", 12);
        this.notes = new ArrayList<>();
        this.notes.add("📌 สรุป Lecture 4: OOP Concepts\n- Encapsulation: ซ่อนฟิลด์ด้วย private ป้องกันไม่ให้แบตเตอรี่ลดลง\n- Inheritance: Smartphone extends Device\n- Polymorphism: App.renderContent() แสดงผลต่างกันในแต่ละแอป\n- Abstraction: Device เป็น abstract class กำหนดโครงร่าง");
        this.notes.add("📌 สรุป Lecture 5.2: MVC Pattern\n- Model: คลาส Device, Smartphone, AndroidPhone\n- View: PhoneWebView สร้าง HTML/CSS สำหรับจำลองหน้าจอบน Localhost\n- Controller: PhoneController รับ Action แปลงเป็นเหตุการณ์ในระบบ");
        this.notes.add("📌 สรุป Lecture 6: Multitasking\n- ClockThread: Background Thread อัปเดตเวลานาฬิกาทุกวินาที\n- MultitaskService: รันงานหลายอย่างพร้อมกันโดยไม่ให้ UI ค้าง");
    }

    @Override
    public String renderContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class='notes-container'>");
        sb.append("<div class='notes-head'><h3>📝 บันทึกสรุปบทเรียน</h3></div>");
        sb.append("<div class='notes-list'>");
        for (String note : notes) {
            sb.append("<div class='note-item'><pre>").append(note).append("</pre></div>");
        }
        sb.append("</div></div>");
        return sb.toString();
    }

    @Override
    public String performAction(String actionParam) {
        if (actionParam != null && !actionParam.isBlank()) {
            notes.add(0, "📌 โน้ตใหม่: " + actionParam);
            return "เพิ่มโน้ตสำเร็จ";
        }
        return "Notes Ready";
    }
}
