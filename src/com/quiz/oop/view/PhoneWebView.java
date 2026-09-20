package com.quiz.oop.view;

import com.quiz.oop.model.AndroidPhone;
import com.quiz.oop.model.app.App;

/**
 * ============================================================================
 * [Lecture 5.2: MVC Architectural Pattern - VIEW]
 * PhoneWebView: ทำหน้าที่สร้างและเรนเดอร์หน้าจอผู้ใช้ (UI / HTML / CSS)
 * ----------------------------------------------------------------------------
 * - มือถือเพียงเครื่องเดียว (ไม่มีแถบเลือก)
 * - แบตเตอรี่ 100% ตลอดเวลา (ไม่มีวันลด)
 * - ปลดล็อกอัตโนมัติ (ไม่มีระบบใส่รหัส)
 * ============================================================================
 */
public class PhoneWebView {

    public static String renderPage(AndroidPhone phone, String currentTime) {
        StringBuilder appGridHtml = new StringBuilder();
        for (App app : phone.getInstalledApps()) {
            appGridHtml.append("<div class='app-icon-item' onclick='triggerEvent(\"launch_app\", \"")
                       .append(app.getPackageName()).append("\")'>")
                       .append("<div class='app-icon-box'>").append(app.getIcon()).append("</div>")
                       .append("<span class='app-name-label'>").append(app.getName().split(" ")[0]).append("</span>")
                       .append("</div>");
        }

        StringBuilder notifListHtml = new StringBuilder();
        if (phone.getNotifications().isEmpty()) {
            notifListHtml.append("<div style='text-align:center; padding:20px; color:#64748b;'>ไม่มีการแจ้งเตือน</div>");
        } else {
            for (String n : phone.getNotifications()) {
                notifListHtml.append("<div class='notif-card'>")
                             .append("<div class='notif-icon'>🔔</div>")
                             .append("<div class='notif-content'>")
                             .append("<div class='notif-desc'>").append(n).append("</div>")
                             .append("</div></div>");
            }
        }

        boolean isPowered = phone.isPoweredOn();
        App activeApp = phone.getCurrentApp();
        String screenOffClass = isPowered ? "" : "screen-off";

        StringBuilder activeAppHtml = new StringBuilder();
        if (activeApp != null) {
            activeAppHtml.append("<div class='app-view-window active' id='appWindow'>")
                         .append("<div class='app-view-header'>")
                         .append("<div class='app-header-left'><span>").append(activeApp.getIcon()).append("</span> <span>").append(activeApp.getName()).append("</span></div>")
                         .append("<button class='btn-close-app' onclick='triggerEvent(\"close_app\", \"\")'>✕</button>")
                         .append("</div>")
                         .append("<div class='app-view-body'>")
                         .append(activeApp.renderContent())
                         .append("</div></div>");
        }

        String displayTime = currentTime != null && currentTime.length() >= 5 ? currentTime.substring(0, 5) : "12:00";

        String template = """
            <!DOCTYPE html>
            <html lang="th">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Android Smartphone OOP Simulator (Java Edition)</title>
                <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body {
                        background-color: #060207;
                        color: #fdf2f8;
                        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
                        min-height: 100vh;
                        display: flex;
                        flex-direction: column;
                    }
                    .top-navbar {
                        background: #110514;
                        border-bottom: 1px solid #2d0c2c;
                        padding: 14px 24px;
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                    }
                    .brand-header {
                        display: flex;
                        align-items: center;
                        gap: 12px;
                    }
                    .brand-header h1 { font-size: 18px; color: #fff0f7; }
                    .badge-tag {
                        background: linear-gradient(135deg, #ff2a85, #c2185b);
                        color: #fff;
                        font-size: 11px;
                        font-weight: 700;
                        padding: 3px 8px;
                        border-radius: 6px;
                        box-shadow: 0 0 10px rgba(255, 42, 133, 0.4);
                    }
                    .status-pill {
                        display: flex;
                        align-items: center;
                        gap: 8px;
                        background: #1c0822;
                        border: 1px solid #3d1240;
                        padding: 6px 12px;
                        border-radius: 20px;
                        font-size: 12px;
                        color: #ff75b5;
                    }
                    .status-dot {
                        width: 8px; height: 8px;
                        border-radius: 50%;
                        background: #ff2a85;
                        box-shadow: 0 0 8px #ff2a85;
                    }
                    .main-container {
                        flex: 1;
                        display: flex;
                        flex-direction: column;
                        align-items: center;
                        justify-content: center;
                        padding: 24px 16px;
                    }
                    /* กรอบโทรศัพท์ Android */
                    .phone-chassis {
                        position: relative;
                        padding: 20px 30px;
                    }
                    .android-phone {
                        position: relative;
                        width: 360px;
                        height: 720px;
                        background: #000;
                        border-radius: 42px;
                        box-shadow: 0 0 0 12px #160819, 0 0 0 14px #2c102c, 0 25px 60px rgba(0,0,0,0.85), 0 0 35px rgba(255, 42, 133, 0.12);
                        overflow: hidden;
                        user-select: none;
                    }
                    /* ปุ่มกายภาพรอบตัวเครื่อง */
                    .hw-btn-power {
                        position: absolute; right: 13px; top: 180px;
                        width: 6px; height: 55px;
                        background: #ff2a85; border-radius: 0 4px 4px 0;
                        box-shadow: 0 0 10px #ff2a85;
                        cursor: pointer;
                    }
                    .hw-btn-vol-up {
                        position: absolute; left: 13px; top: 160px;
                        width: 6px; height: 48px;
                        background: #2b112d; border-radius: 4px 0 0 4px;
                        cursor: pointer;
                    }
                    .hw-btn-vol-down {
                        position: absolute; left: 13px; top: 220px;
                        width: 6px; height: 48px;
                        background: #2b112d; border-radius: 4px 0 0 4px;
                        cursor: pointer;
                    }
                    .punch-hole {
                        position: absolute; top: 11px; left: 50%;
                        transform: translateX(-50%);
                        width: 13px; height: 13px;
                        background: #030104; border: 1.5px solid #1c0822;
                        border-radius: 50%; z-index: 999;
                    }
                    .phone-screen {
                        width: 100%; height: 100%;
                        background: linear-gradient(to bottom, #1d0722, #0d0310, #020104);
                        display: flex; flex-direction: column;
                        position: relative; overflow: hidden;
                    }
                    .phone-screen.screen-off {
                        background: #000 !important;
                    }
                    .phone-screen.screen-off > *:not(.punch-hole) {
                        display: none !important;
                    }
                    /* Status Bar */
                    .status-bar {
                        height: 34px;
                        display: flex; justify-content: space-between; align-items: center;
                        padding: 6px 18px 0;
                        font-size: 11px; font-weight: 600;
                        z-index: 50;
                        color: #fbcfe8;
                        background: linear-gradient(to bottom, rgba(16,3,19,0.8), transparent);
                    }
                    .battery-badge {
                        display: flex; align-items: center; gap: 4px;
                    }
                    .battery-shell {
                        position: relative; width: 20px; height: 10px;
                        border: 1.5px solid #ff75b5; border-radius: 3px; padding: 1px;
                    }
                    .battery-shell::after {
                        content:''; position: absolute; right: -3px; top: 2px;
                        width: 2px; height: 4px; background: #ff75b5; border-radius: 0 1px 1px 0;
                    }
                    .battery-fill {
                        height: 100%; background: #ff2a85; width: 100%;
                        box-shadow: 0 0 6px #ff2a85;
                        border-radius: 1px;
                    }
                    /* Home Screen */
                    .home-screen {
                        flex: 1; display: flex; flex-direction: column;
                        justify-content: space-between; padding: 14px 16px;
                    }
                    .home-clock-widget {
                        padding: 10px 4px;
                    }
                    .home-time { font-size: 38px; font-weight: 300; color: #fff; text-shadow: 0 0 15px rgba(255, 42, 133, 0.4); }
                    .home-date { font-size: 13px; color: #f472b6; }
                    .home-brand-tag {
                        font-size: 11px; color: #ff2a85; font-weight: 800;
                        text-transform: uppercase; margin-top: 2px;
                        letter-spacing: 1px;
                    }
                    .search-bar-widget {
                        background: rgba(255, 42, 133, 0.12);
                        backdrop-filter: blur(10px);
                        border: 1px solid rgba(255, 42, 133, 0.25);
                        border-radius: 20px; padding: 9px 14px;
                        display: flex; justify-content: space-between;
                        font-size: 12px; color: #fbcfe8; margin: 12px 0 16px;
                    }
                    .app-grid {
                        display: grid; grid-template-columns: repeat(4, 1fr);
                        gap: 16px 8px;
                    }
                    .app-icon-item {
                        display: flex; flex-direction: column; align-items: center; gap: 6px;
                        cursor: pointer; transition: transform 0.15s;
                    }
                    .app-icon-item:hover { transform: translateY(-2px); }
                    .app-icon-item:active { transform: scale(0.92); }
                    .app-icon-box {
                        width: 52px; height: 52px; border-radius: 16px;
                        background: rgba(255, 42, 133, 0.14); backdrop-filter: blur(12px);
                        display: flex; align-items: center; justify-content: center;
                        font-size: 26px; border: 1px solid rgba(255, 42, 133, 0.28);
                        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
                    }
                    .app-name-label {
                        font-size: 11px; color: #fce7f3; text-align: center; font-weight: 500;
                    }
                    .home-dock {
                        margin-top: auto; padding: 10px 14px;
                        background: rgba(18, 5, 22, 0.7); backdrop-filter: blur(15px);
                        border: 1px solid rgba(255, 42, 133, 0.22);
                        border-radius: 24px; display: flex; justify-content: space-around;
                    }
                    .android-nav-bar {
                        height: 38px; background: rgba(8, 1, 10, 0.85);
                        border-top: 1px solid rgba(255, 42, 133, 0.15);
                        display: flex; justify-content: space-around; align-items: center;
                        z-index: 500;
                    }
                    .nav-btn {
                        background: none; border: none; color: #f472b6;
                        font-size: 16px; cursor: pointer; padding: 6px 20px;
                    }
                    /* App Window View */
                    .app-view-window {
                        position: absolute; inset: 34px 0 38px 0;
                        background: #080209; z-index: 250;
                        display: flex; flex-direction: column;
                    }
                    .app-view-header {
                        height: 48px; padding: 0 14px;
                        display: flex; justify-content: space-between; align-items: center;
                        background: rgba(22, 6, 26, 0.95); border-bottom: 1px solid rgba(255, 42, 133, 0.3);
                        color: #ff75b5;
                    }
                    .app-header-left { display: flex; align-items: center; gap: 8px; font-weight: 700; font-size: 14px; color: #fdf2f8; }
                    .btn-close-app { background: none; border: none; color: #ff75b5; font-size: 18px; cursor: pointer; }
                    .app-view-body { flex: 1; padding: 14px; overflow-y: auto; }
                    /* App Specific Styles */
                    .camera-container { display: flex; flex-direction: column; height: 100%; }
                    .camera-viewfinder {
                        flex: 1; background: #020104; border-radius: 14px;
                        display: flex; flex-direction: column; justify-content: space-between;
                        padding: 12px; border: 1px solid #2b112d;
                    }
                    .camera-hud { display: flex; justify-content: space-between; font-size: 10px; color: #f472b6; }
                    .camera-status-text { text-align: center; font-size: 12px; color: #ff75b5; }
                    .camera-shutter-bar {
                        display: flex; justify-content: space-around; align-items: center;
                        padding: 16px 0 6px;
                    }
                    .btn-cam-shutter {
                        width: 60px; height: 60px; border-radius: 50%;
                        background: #ff2a85; border: 4px solid #4a133b; cursor: pointer;
                        box-shadow: 0 0 15px #ff2a85;
                    }
                    .btn-cam-action {
                        background: #240c29; border: 1px solid #4a133b; color: #fff;
                        padding: 8px 12px; border-radius: 20px; font-size: 11px; cursor: pointer;
                    }
                    .music-container { display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100%; text-align: center; }
                    .music-record {
                        width: 150px; height: 150px; border-radius: 50%;
                        background: radial-gradient(circle, #2d0b30 20%, #060108 100%);
                        display: flex; align-items: center; justify-content: center;
                        margin-bottom: 20px; border: 3px solid #ff2a85;
                        box-shadow: 0 0 25px rgba(255, 42, 133, 0.4);
                    }
                    .music-record.spinning { animation: spin 8s linear infinite; }
                    @keyframes spin { to { transform: rotate(360deg); } }
                    .record-center { font-size: 34px; }
                    .music-meta h3 { font-size: 15px; margin-bottom: 4px; color: #fdf2f8; }
                    .music-meta p { font-size: 12px; color: #ff75b5; margin-bottom: 16px; }
                    .music-progress { width: 100%; height: 4px; background: #260a2b; border-radius: 2px; margin-bottom: 18px; }
                    .music-progress-bar { height: 100%; background: #ff2a85; box-shadow: 0 0 8px #ff2a85; border-radius: 2px; }
                    .music-btns { display: flex; gap: 16px; align-items: center; }
                    .music-btn {
                        background: #1c0822; border: 1px solid rgba(255, 42, 133, 0.3); color: #ff75b5;
                        width: 44px; height: 44px; border-radius: 50%; font-size: 16px; cursor: pointer;
                    }
                    .music-btn.play-btn { width: 56px; height: 56px; background: #ff2a85; color: #fff; font-size: 20px; box-shadow: 0 0 15px #ff2a85; }
                    .settings-container { display: flex; flex-direction: column; gap: 12px; }
                    .setting-group { background: #18081f; border: 1px solid rgba(255, 42, 133, 0.2); border-radius: 12px; padding: 12px; font-size: 12px; }
                    .setting-group-title { font-size: 11px; font-weight: 700; color: #ff75b5; text-transform: uppercase; margin-bottom: 8px; }
                    .setting-row { display: flex; justify-content: space-between; padding: 6px 0; border-bottom: 1px solid rgba(255, 42, 133, 0.1); }
                    .setting-row:last-child { border-bottom: none; }
                    .notes-container { display: flex; flex-direction: column; gap: 10px; }
                    .note-item { background: #18081f; border-left: 3px solid #ff2a85; padding: 10px; border-radius: 8px; }
                    .note-item pre { white-space: pre-wrap; font-family: inherit; font-size: 11px; color: #fce7f3; }

                    /* Custom Modern Glassmorphism Auth Portal Styles - Pink & Black Theme */
                    .custom-portal-card {
                        background: linear-gradient(150deg, #190720, #08020b);
                        border: 1.5px solid rgba(255, 42, 133, 0.35);
                        border-radius: 24px;
                        padding: 22px 18px;
                        box-shadow: 0 20px 45px rgba(0, 0, 0, 0.9), 0 0 30px rgba(255, 42, 133, 0.15), inset 0 1px 0 rgba(255, 117, 181, 0.2);
                        color: #fff;
                    }
                    .portal-brand-banner {
                        text-align: center;
                        margin-bottom: 18px;
                    }
                    .portal-emblem-icon {
                        width: 50px;
                        height: 50px;
                        border-radius: 16px;
                        background: linear-gradient(135deg, #ff2a85, #b5179e);
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        font-size: 26px;
                        margin: 0 auto 10px;
                        box-shadow: 0 6px 20px rgba(255, 42, 133, 0.5);
                    }
                    .portal-brand-title {
                        font-size: 19px;
                        font-weight: 800;
                        background: linear-gradient(to right, #ffffff, #ff75b5, #ff2a85);
                        -webkit-background-clip: text;
                        -webkit-text-fill-color: transparent;
                        letter-spacing: 1.2px;
                    }
                    .portal-brand-subtitle {
                        font-size: 11px;
                        color: #f472b6;
                        margin-top: 3px;
                        font-weight: 500;
                    }
                    .modern-pill-tabs {
                        display: flex;
                        background: rgba(9, 2, 12, 0.85);
                        border: 1.5px solid rgba(255, 42, 133, 0.28);
                        border-radius: 30px;
                        padding: 4px;
                        margin-bottom: 18px;
                        gap: 4px;
                    }
                    .pill-tab-btn {
                        flex: 1;
                        padding: 9px 8px;
                        border-radius: 24px;
                        border: none;
                        background: transparent;
                        color: #f472b6;
                        font-size: 12px;
                        font-weight: 700;
                        cursor: pointer;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        gap: 6px;
                        transition: all 0.25s ease;
                    }
                    .pill-tab-btn.active {
                        background: linear-gradient(135deg, #ff2a85, #c2185b);
                        color: #ffffff;
                        box-shadow: 0 4px 14px rgba(255, 42, 133, 0.6);
                    }
                    .modern-form-group {
                        margin-bottom: 14px;
                    }
                    .modern-form-label {
                        display: flex;
                        align-items: center;
                        gap: 6px;
                        font-size: 12px;
                        font-weight: 600;
                        color: #ffe4e6;
                        margin-bottom: 6px;
                    }
                    .modern-input-box {
                        position: relative;
                    }
                    .modern-form-input {
                        width: 100%;
                        padding: 12px 14px;
                        background: rgba(13, 3, 17, 0.75);
                        border: 1.5px solid rgba(255, 42, 133, 0.28);
                        border-radius: 12px;
                        color: #ffffff;
                        font-size: 13px;
                        outline: none;
                        transition: all 0.2s ease;
                    }
                    .modern-form-input:focus {
                        border-color: #ff2a85;
                        box-shadow: 0 0 14px rgba(255, 42, 133, 0.45);
                        background: rgba(19, 4, 24, 0.95);
                    }
                    .modern-form-input::placeholder {
                        color: #7b436c;
                    }
                    .btn-portal-action {
                        width: 100%;
                        background: linear-gradient(135deg, #ff2a85, #be185d);
                        color: #ffffff;
                        font-weight: 800;
                        font-size: 14px;
                        border-radius: 14px;
                        padding: 13px;
                        border: none;
                        box-shadow: 0 6px 20px rgba(255, 42, 133, 0.5);
                        cursor: pointer;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        gap: 8px;
                        margin-top: 8px;
                        transition: all 0.2s ease;
                    }
                    .btn-portal-action:hover {
                        transform: translateY(-2px);
                        box-shadow: 0 8px 25px rgba(255, 42, 133, 0.7);
                    }
                    .btn-portal-action:active {
                        transform: scale(0.98);
                    }
                    .custom-portal-footer {
                        text-align: center;
                        margin-top: 16px;
                        font-size: 11px;
                        color: #f472b6;
                    }
                    .custom-portal-footer a {
                        color: #ff75b5;
                        text-decoration: none;
                        font-weight: 700;
                        border-bottom: 1px dashed #ff75b5;
                    }
                    .portal-demo-pill {
                        margin-top: 16px;
                        background: rgba(28, 8, 33, 0.7);
                        border: 1px solid rgba(255, 42, 133, 0.25);
                        padding: 8px 12px;
                        border-radius: 10px;
                        font-size: 10.5px;
                        color: #fbcfe8;
                        text-align: center;
                    }
                    .portal-alert-box {
                        padding: 8px 12px;
                        border-radius: 10px;
                        font-size: 11.5px;
                        font-weight: 600;
                        text-align: center;
                        margin-bottom: 12px;
                        background: rgba(255, 42, 133, 0.1);
                        border: 1px solid;
                    }

                    /* Quiz Game Gameplay Styles - Pink & Black Theme */
                    .quiz-game-container {
                        display: flex;
                        flex-direction: column;
                        gap: 12px;
                    }
                    .quiz-status-header {
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        background: #18071e;
                        border: 1px solid rgba(255, 42, 133, 0.3);
                        padding: 8px 12px;
                        border-radius: 12px;
                        font-size: 12px;
                    }
                    .user-tag { font-weight: 700; color: #ff75b5; }
                    .quiz-score-badge { font-weight: 800; color: #ff2a85; }
                    .btn-quiz-logout {
                        background: #2b0b28; border: 1px solid rgba(255, 42, 133, 0.3); color: #ff75b5;
                        padding: 4px 8px; border-radius: 6px; font-size: 11px; cursor: pointer;
                    }
                    .quiz-progress-bar-wrap {
                        height: 5px; background: #1d0724; border-radius: 3px; overflow: hidden;
                    }
                    .quiz-progress-bar-fill {
                        height: 100%; background: linear-gradient(90deg, #ff2a85, #ff75b5);
                        box-shadow: 0 0 10px #ff2a85; transition: width 0.3s;
                    }
                    .quiz-question-box {
                        background: #14051a;
                        border: 1.5px solid rgba(255, 42, 133, 0.3);
                        border-radius: 14px;
                        padding: 14px;
                    }
                    .q-number-tag {
                        font-size: 10px; font-weight: 800; color: #ff75b5; text-transform: uppercase;
                    }
                    .q-text {
                        font-size: 14px; color: #fff0f7; margin-top: 6px; line-height: 1.5;
                    }
                    .quiz-choices-list {
                        display: flex; flex-direction: column; gap: 8px;
                    }
                    .quiz-choice-btn {
                        background: #16061b; border: 1.5px solid rgba(255, 42, 133, 0.25);
                        color: #fdf2f8; padding: 12px 14px; border-radius: 10px;
                        font-size: 13px; text-align: left; cursor: pointer; transition: all 0.15s;
                    }
                    .quiz-choice-btn:hover { background: #260a2d; border-color: #ff2a85; }
                    .quiz-choice-btn.choice-correct {
                        background: #3b0d28 !important; border-color: #ff2a85 !important; color: #ffd6ea !important;
                        box-shadow: 0 0 14px rgba(255, 42, 133, 0.5) !important;
                    }
                    .quiz-choice-btn.choice-wrong {
                        background: #2d080e !important; border-color: #f43f5e !important; color: #fecdd3 !important;
                    }
                    .quiz-explanation-box {
                        background: #190720; border-radius: 10px; padding: 10px 12px; font-size: 12px;
                        border: 1px solid rgba(255, 42, 133, 0.25); color: #fce7f3;
                    }
                    .btn-next-question {
                        background: linear-gradient(135deg, #ff2a85, #be185d); color: #fff; border: none; padding: 12px;
                        border-radius: 10px; font-weight: 700; font-size: 13px; cursor: pointer;
                        box-shadow: 0 4px 16px rgba(255, 42, 133, 0.5);
                    }
                    .quiz-result-container {
                        text-align: center; padding: 24px 12px;
                        display: flex; flex-direction: column; align-items: center; gap: 14px;
                    }
                    .result-student-id { font-size: 13px; color: #f472b6; }
                    .result-score-circle {
                        width: 120px; height: 120px; border-radius: 50%;
                        background: radial-gradient(circle, #ff2a85 20%, #150519 100%);
                        display: flex; flex-direction: column; align-items: center; justify-content: center;
                        box-shadow: 0 0 30px rgba(255, 42, 133, 0.6);
                        border: 2px solid rgba(255, 42, 133, 0.5);
                    }
                    .result-score-number { font-size: 36px; font-weight: 800; color: #fff; text-shadow: 0 0 10px #ff75b5; }
                    .result-score-total { font-size: 11px; color: #fbcfe8; }
                    .result-grade-badge {
                        background: #1c0822; border: 1.5px solid #ff2a85;
                        padding: 8px 16px; border-radius: 20px; font-weight: 700; color: #ff75b5; font-size: 13px;
                        box-shadow: 0 0 12px rgba(255, 42, 133, 0.35);
                    }
                    .result-actions { display: flex; gap: 10px; width: 100%; }
                    .btn-restart-quiz {
                        flex: 1; background: linear-gradient(135deg, #ff2a85, #be185d); border: none; color: #fff;
                        padding: 12px; border-radius: 10px; font-weight: 700; font-size: 13px; cursor: pointer;
                        box-shadow: 0 4px 14px rgba(255, 42, 133, 0.4);
                    }
                    .btn-result-logout {
                        background: #1c0822; border: 1px solid rgba(255, 42, 133, 0.3); color: #fbcfe8;
                        padding: 12px; border-radius: 10px; font-weight: 700; font-size: 13px; cursor: pointer;
                    }
                    .notif-shade {
                        position: absolute; top: 0; left: 0; right: 0; height: 75%;
                        background: rgba(14,3,18,0.96); backdrop-filter: blur(20px);
                        border-bottom: 2px solid #ff2a85;
                        border-radius: 0 0 28px 28px; z-index: 400; padding: 40px 16px 16px;
                        transform: translateY(-100%); transition: transform 0.3s cubic-bezier(0.16, 1, 0.3, 1);
                        display: flex; flex-direction: column;
                    }
                    .notif-shade.open { transform: translateY(0); }
                    .shade-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
                    .btn-clear-notif { background: none; border: none; color: #ff75b5; font-size: 12px; cursor: pointer; }
                    .notif-card { background: #1b0722; border: 1px solid rgba(255, 42, 133, 0.25); border-radius: 10px; padding: 10px; display: flex; gap: 10px; margin-bottom: 8px; }
                    .notif-desc { font-size: 12px; color: #fdf2f8; }
                    .hints-bar { margin-top: 14px; display: flex; gap: 12px; font-size: 12px; color: #f472b6; }
                    .hints-bar span { background: #14051a; border: 1px solid #2e0d30; padding: 4px 10px; border-radius: 6px; }
                </style>
            </head>
            <body>
                <header class="top-navbar">
                    <div class="brand-header">
                        <span style="font-size: 24px;">☕</span>
                        <div>
                            <h1>Android Smartphone OOP Simulator</h1>
                            <span style="font-size: 11px; color: #ff75b5;">100% Pure Java Architecture &bull; Pink & Black Edition</span>
                        </div>
                    </div>
                    <div style="display: flex; align-items: center; gap: 10px;">
                        <div class="status-pill" id="firebaseStatusPill" style="border-color: rgba(255, 42, 133, 0.4); background: #1a0820;">
                            <span class="status-dot" style="background: #ff2a85; box-shadow: 0 0 8px #ff2a85;"></span>
                            <span id="firebaseStatusText">🔥 Firebase: projectquiz-5b3cd</span>
                        </div>
                        <div class="status-pill">
                            <span class="status-dot"></span>
                            <span>Java 21 (Port 3000)</span>
                        </div>
                    </div>
                </header>

                <main class="main-container">
                    <div class="phone-chassis">
                        <!-- ปุ่มจริงรอบตัวเครื่อง (Hardware Physical Buttons) -->
                        <div class="hw-btn-vol-up" onclick="triggerEvent('volume_up', '')" title="เพิ่มเสียง (Volume Up)"></div>
                        <div class="hw-btn-vol-down" onclick="triggerEvent('volume_down', '')" title="ลดเสียง (Volume Down)"></div>
                        <div class="hw-btn-power" onclick="triggerEvent('power_toggle', '')" title="ปุ่มเปิด/ปิดเครื่อง (Power Button)"></div>

                        <div class="android-phone">
                            <div class="punch-hole"></div>

                            <div class="phone-screen {{SCREEN_OFF_CLASS}}" id="phoneScreen">
                                <!-- Status Bar (แบตเตอรี่ไม่มีวันลด: 100% ตลอดเวลา) -->
                                <div class="status-bar">
                                    <span>{{DISPLAY_TIME}}</span>
                                    <div style="display: flex; align-items: center; gap: 8px;">
                                        <span>📶 5G</span>
                                        <div class="battery-badge">
                                            <span style="color: #ff75b5; font-weight: 700;">100%</span>
                                            <div class="battery-shell">
                                                <div class="battery-fill"></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- Home Screen (เข้าได้ทันที ไม่ต้องใส่รหัส) -->
                                <div class="home-screen">
                                    <div class="home-clock-widget">
                                        <div class="home-time">{{DISPLAY_TIME}}</div>
                                        <div class="home-date">วันจันทร์, 20 กันยายน</div>
                                        <div class="home-brand-tag">{{BRAND}} {{MODEL}}</div>
                                    </div>

                                    <div class="search-bar-widget">
                                        <span>🔍 ค้นหาในโทรศัพท์...</span>
                                        <span>🎤 📷</span>
                                    </div>

                                    <!-- App Grid -->
                                    <div class="app-grid">
                                        {{APP_GRID}}
                                    </div>

                                    <!-- Bottom Dock -->
                                    <div class="home-dock">
                                        <div class="app-icon-box" style="background:#22c55e;">📞</div>
                                        <div class="app-icon-box" style="background:#0284c7;">💬</div>
                                        <div class="app-icon-box" style="background:#f59e0b;">🌐</div>
                                        <div class="app-icon-box" style="background:#ef4444;" onclick="triggerEvent('launch_app', 'com.android.camera')">📷</div>
                                    </div>
                                </div>

                                <!-- Active App Window (ถ้าเปิดแอปอยู่) -->
                                {{ACTIVE_APP}}

                                <!-- Notification Shade -->
                                <div class="notif-shade" id="notifShade">
                                    <div class="shade-head">
                                        <span style="font-size: 13px; font-weight: 700;">การแจ้งเตือน (Notifications)</span>
                                        <button class="btn-clear-notif" onclick="triggerEvent('clear_notifs', '')">ล้างทั้งหมด</button>
                                    </div>
                                    <div style="flex: 1; overflow-y: auto;">
                                        {{NOTIF_LIST}}
                                    </div>
                                </div>

                                <!-- Navigation Bar -->
                                <div class="android-nav-bar">
                                    <button class="nav-btn" onclick="triggerEvent('nav_back', '')" title="ย้อนกลับ">◀</button>
                                    <button class="nav-btn" onclick="triggerEvent('nav_home', '')" title="หน้าจอหลัก">⭕</button>
                                    <button class="nav-btn" onclick="toggleShade()" title="การแจ้งเตือน">▢</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="hints-bar">
                        <span>⚡ ปุ่มขวา: เปิด/ปิดเครื่อง</span>
                        <span>🔊 ปุ่มซ้าย: เพิ่ม/ลดเสียง</span>
                        <span>🔔 ปุ่ม ▢: แถบแจ้งเตือน</span>
                        <span>🔋 แบตเตอรี่: 100% ตลอดเวลา</span>
                    </div>
                </main>

                <!-- Firebase App, Analytics & Cloud Firestore SDK (Modular CDN) -->
                <script type="module">
                    import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-app.js";
                    import { getAnalytics, logEvent } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-analytics.js";
                    import { 
                        getFirestore, 
                        doc, 
                        setDoc, 
                        getDoc, 
                        collection, 
                        addDoc,
                        deleteDoc 
                    } from "https://www.gstatic.com/firebasejs/10.12.0/firebase-firestore.js";

                    // Your web app's Firebase configuration
                    const firebaseConfig = {
                        apiKey: "AIzaSyCYUkS3DaKsEzRAkVXrKiz3RzI44P4fM9g",
                        authDomain: "projectquiz-5b3cd.firebaseapp.com",
                        projectId: "projectquiz-5b3cd",
                        storageBucket: "projectquiz-5b3cd.firebasestorage.app",
                        messagingSenderId: "476675572289",
                        appId: "1:476675572289:web:7d0faaeacc260c7593fcc0",
                        measurementId: "G-HKEWN731EZ"
                    };

                    try {
                        const app = initializeApp(firebaseConfig);
                        let analytics = null;
                        try {
                            analytics = getAnalytics(app);
                            logEvent(analytics, 'page_view', { page_title: 'Android Phone Simulator' });
                        } catch (aErr) {
                            console.warn("Firebase Analytics notice:", aErr);
                        }

                        const db = getFirestore(app);

                        window.firebaseApp = app;
                        window.firebaseAnalytics = analytics;
                        window.firebaseDb = db;

                        window.logFirebaseEvent = function(eventName, params) {
                            console.log("🔥 [Firebase LogEvent]:", eventName, params || {});
                            if (analytics) {
                                try { logEvent(analytics, eventName, params); } catch (e) {}
                            }
                        };

                        // บันทึกผู้ใช้ลง Firestore
                        window.saveUserToFirestore = async function(studentId, password) {
                            try {
                                const isAdmin = (studentId === "6812247008");
                                await setDoc(doc(db, "users", studentId), {
                                    studentId: studentId,
                                    password: password,
                                    role: isAdmin ? "admin" : "student",
                                    createdAt: new Date().toISOString(),
                                    highScore: 0,
                                    totalScore: 0,
                                    quizzesPlayed: 0
                                }, { merge: true });
                                console.log("🔥 [Firestore] User saved successfully:", studentId, "Role:", isAdmin ? "admin" : "student");
                                return { success: true };
                            } catch (err) {
                                console.error("🔥 [Firestore Error] Failed to save user:", err);
                                return { success: false, error: err };
                            }
                        };

                        // บันทึกคะแนนลง Firestore
                        window.saveQuizScoreToFirestore = async function(studentId, score, total) {
                            try {
                                const userRef = doc(db, "users", studentId);
                                const snap = await getDoc(userRef);
                                let currentTotal = 0;
                                let currentHigh = 0;
                                let currentPlayed = 0;
                                if (snap.exists()) {
                                    const data = snap.data();
                                    currentTotal = data.totalScore || 0;
                                    currentHigh = data.highScore || 0;
                                    currentPlayed = data.quizzesPlayed || 0;
                                }

                                await setDoc(userRef, {
                                    lastScore: score,
                                    totalQuestions: total,
                                    totalScore: currentTotal + score,
                                    highScore: Math.max(currentHigh, score),
                                    quizzesPlayed: currentPlayed + 1,
                                    lastQuizAt: new Date().toISOString()
                                }, { merge: true });

                                await addDoc(collection(db, "quiz_history"), {
                                    studentId: studentId,
                                    score: score,
                                    total: total,
                                    percentage: Math.round((score / total) * 100),
                                    completedAt: new Date().toISOString()
                                });
                                console.log("🔥 [Firestore] Score recorded for:", studentId);
                            } catch (err) {
                                console.error("🔥 [Firestore Error] Failed to record quiz score:", err);
                            }
                        };

                        // บันทึกข้อสอบใหม่ที่ Admin เพิ่มลง Firestore
                        window.saveCustomQuestionToFirestore = async function(qData) {
                            try {
                                const docRef = await addDoc(collection(db, "questions"), {
                                    ...qData,
                                    createdAt: new Date().toISOString()
                                });
                                console.log("🔥 [Firestore] Question saved with ID:", docRef.id);
                                return { success: true, id: docRef.id };
                            } catch (err) {
                                console.error("🔥 [Firestore Error] Failed to save question:", err);
                                return { success: false, error: err };
                            }
                        };

                        // ลบข้อสอบออกจาก Firestore
                        window.deleteQuestionFromFirestore = async function(docId) {
                            try {
                                if (docId) {
                                    await deleteDoc(doc(db, "questions", docId));
                                    console.log("🔥 [Firestore] Question deleted:", docId);
                                    return { success: true };
                                }
                                return { success: false, error: "Missing docId" };
                            } catch (err) {
                                console.error("🔥 [Firestore Error] Failed to delete question:", err);
                                return { success: false, error: err };
                            }
                        };

                        // ซิงก์ Admin 6812247008 และ demo user เข้า Firestore
                        setTimeout(async () => {
                            try {
                                // Seed Admin (คะแนนเริ่มต้น 0 จริง ไม่เมคคะแนน)
                                await setDoc(doc(db, "users", "6812247008"), {
                                    studentId: "6812247008",
                                    password: "12345678",
                                    role: "admin",
                                    createdAt: new Date().toISOString(),
                                    highScore: 0,
                                    totalScore: 0,
                                    quizzesPlayed: 0
                                }, { merge: true });

                                // Seed Demo Player (คะแนนเริ่มต้น 0 จริง)
                                await setDoc(doc(db, "users", "6812345678"), {
                                    studentId: "6812345678",
                                    password: "12345678",
                                    role: "student",
                                    createdAt: new Date().toISOString(),
                                    highScore: 0,
                                    totalScore: 0,
                                    quizzesPlayed: 0
                                }, { merge: true });

                                console.log("🔥 [Firestore] Admin '6812247008' & Demo user synced!");
                            } catch (err) {
                                console.warn("Firestore seed note:", err.message);
                            }
                        }, 800);

                        console.log("🔥 Firebase initialized successfully:", firebaseConfig.projectId);
                        const statusElem = document.getElementById('firebaseStatusText');
                        if (statusElem) {
                            statusElem.innerHTML = "🔥 Firestore: Connected (" + firebaseConfig.projectId + ")";
                        }
                    } catch (err) {
                        console.error("Firebase init failed:", err);
                        const statusElem = document.getElementById('firebaseStatusText');
                        if (statusElem) {
                            statusElem.innerText = "⚠️ Firebase: Error";
                        }
                    }
                </script>

                <script>
                    function triggerEvent(actionType, param) {
                        if (window.logFirebaseEvent) {
                            window.logFirebaseEvent('phone_action_' + actionType, { param: String(param || '') });
                        }
                        fetch('/api/action?type=' + encodeURIComponent(actionType) + '&param=' + encodeURIComponent(param))
                            .then(res => res.json())
                            .then(data => {
                                if (data.reload) {
                                    window.location.reload();
                                }
                            })
                            .catch(err => console.error(err));
                    }

                    function toggleShade() {
                        const shade = document.getElementById('notifShade');
                        if (shade) shade.classList.toggle('open');
                    }
                </script>
            </body>
            </html>
            """;

        return template
            .replace("{{SCREEN_OFF_CLASS}}", screenOffClass)
            .replace("{{DISPLAY_TIME}}", displayTime)
            .replace("{{BRAND}}", phone.getBrand())
            .replace("{{MODEL}}", phone.getModel())
            .replace("{{APP_GRID}}", appGridHtml.toString())
            .replace("{{ACTIVE_APP}}", activeAppHtml.toString())
            .replace("{{NOTIF_LIST}}", notifListHtml.toString());
    }
}
