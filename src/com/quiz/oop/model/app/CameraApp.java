package com.quiz.oop.model.app;

/**
 * ============================================================================
 * [Lecture 4: Polymorphism]
 * CameraApp: แอปพลิเคชันกล้องถ่ายรูปพร้อมฟังก์ชันถ่ายภาพ
 * ============================================================================
 */
public class CameraApp extends App {
    private int photoCount = 0;
    private String lastShot = "ยังไม่มีภาพถ่าย";

    public CameraApp() {
        super("กล้อง (Camera)", "com.android.camera", "📷", 45);
    }

    @Override
    public String renderContent() {
        return """
            <div class='camera-container'>
                <div class='camera-viewfinder'>
                    <div class='camera-grid'></div>
                    <div class='camera-hud'>
                        <span>AI 200MP Master Lens</span>
                        <span>HDR+ Active</span>
                    </div>
                    <div class='camera-status-text'>%s</div>
                </div>
                <div class='camera-shutter-bar'>
                    <button class='btn-cam-action' onclick='triggerEvent("camera_action", "filter")'>✨ โหมดสี</button>
                    <button class='btn-cam-shutter' onclick='triggerEvent("camera_action", "snap")'></button>
                    <button class='btn-cam-action' onclick='triggerEvent("camera_action", "switch")'>🔄 สลับกล้อง</button>
                </div>
            </div>
            """.formatted(lastShot);
    }

    @Override
    public String performAction(String actionParam) {
        if ("snap".equals(actionParam)) {
            photoCount++;
            lastShot = "📸 บันทึกภาพที่ #" + photoCount + " (ความละเอียด 200MP, HDR ประมวลผลแล้ว)";
            return lastShot;
        } else if ("filter".equals(actionParam)) {
            return "✨ สลับโทนสี: Vivid Cinematic Look";
        } else if ("switch".equals(actionParam)) {
            return "🔄 สลับระหว่างกล้องหน้า / กล้องหลัง";
        }
        return "กล้องพร้อมใช้งาน";
    }

    public int getPhotoCount() { return photoCount; }
}
