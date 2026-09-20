package com.quiz.oop.controller;

import com.quiz.oop.event.EventDispatcher;
import com.quiz.oop.event.PhoneEvent;
import com.quiz.oop.model.AndroidPhone;
import com.quiz.oop.model.app.App;

/**
 * ============================================================================
 * [Lecture 5.2: MVC Architectural Pattern - CONTROLLER]
 * PhoneController: ทำหน้าที่ควบคุมและประสานงานระหว่าง Model, View และ Event
 * รับคำสั่งการกระทำจากผู้ใช้ แปลงเป็นคำสั่งแก้ไขสถานะใน Model และกระจายเหตุการณ์
 * ============================================================================
 */
public class PhoneController {
    private final AndroidPhone phone;
    private final EventDispatcher eventDispatcher;

    public PhoneController(AndroidPhone phone, EventDispatcher eventDispatcher) {
        this.phone = phone;
        this.eventDispatcher = eventDispatcher;

        // ลงทะเบียน Event Listener สำหรับบันทึก Log ลงระบบ (Lecture 5.1)
        this.eventDispatcher.addListener(event -> {
            System.out.println("[Event-Driven Controller] " + event);
        });
    }

    public AndroidPhone getModel() {
        return phone;
    }

    /**
     * ประมวลผล Action ที่ส่งมาจาก View
     */
    public synchronized boolean handleAction(String actionType, String param) {
        if (actionType == null) return false;

        // บันทึกและกระจายเหตุการณ์ (Lecture 5.1: Event-driven)
        eventDispatcher.dispatch(new PhoneEvent(actionType, param));

        switch (actionType) {
            case "power_toggle" -> {
                phone.powerButton();
                return true;
            }
            case "volume_up" -> {
                phone.adjustVolume(5);
                return true;
            }
            case "volume_down" -> {
                phone.adjustVolume(-5);
                return true;
            }
            case "launch_app" -> {
                return phone.launchApp(param);
            }
            case "close_app", "nav_home", "nav_back" -> {
                phone.closeApp();
                return true;
            }
            case "clear_notifs" -> {
                phone.clearNotifications();
                return true;
            }
            case "camera_action" -> {
                App app = phone.getCurrentApp();
                if (app != null) {
                    String result = app.performAction(param);
                    phone.addNotification(result);
                    return true;
                }
            }
            case "music_action" -> {
                App app = phone.getCurrentApp();
                if (app != null) {
                    String result = app.performAction(param);
                    phone.addNotification(result);
                    return true;
                }
            }
            case "quiz_tab" -> {
                App app = phone.getCurrentApp();
                if (app instanceof com.quiz.oop.model.app.QuizApp quiz) {
                    quiz.setRegisterTab("register".equals(param));
                    return true;
                }
            }
            case "do_login" -> {
                App app = phone.getCurrentApp();
                if (app instanceof com.quiz.oop.model.app.QuizApp quiz) {
                    String[] parts = param.split("\\|", -1);
                    String u = parts.length > 0 ? parts[0] : "";
                    String p = parts.length > 1 ? parts[1] : "";
                    boolean ok = quiz.login(u, p);
                    if (ok) phone.addNotification("เข้าสู่ระบบ CPE Portal สำเร็จ (รหัส " + u + ")");
                    return true;
                }
            }
            case "do_register" -> {
                App app = phone.getCurrentApp();
                if (app instanceof com.quiz.oop.model.app.QuizApp quiz) {
                    String[] parts = param.split("\\|", -1);
                    String u = parts.length > 0 ? parts[0] : "";
                    String p = parts.length > 1 ? parts[1] : "";
                    String cp = parts.length > 2 ? parts[2] : "";
                    boolean ok = quiz.register(u, p, cp);
                    if (ok) phone.addNotification("สมัครสมาชิกใหม่สำเร็จ (รหัส " + u + ")");
                    return true;
                }
            }
            case "quiz_answer" -> {
                App app = phone.getCurrentApp();
                if (app instanceof com.quiz.oop.model.app.QuizApp quiz) {
                    try {
                        int choice = Integer.parseInt(param);
                        quiz.answerQuestion(choice);
                        return true;
                    } catch (NumberFormatException ignored) {}
                }
            }
            case "quiz_next" -> {
                App app = phone.getCurrentApp();
                if (app instanceof com.quiz.oop.model.app.QuizApp quiz) {
                    quiz.nextQuestion();
                    return true;
                }
            }
            case "quiz_restart" -> {
                App app = phone.getCurrentApp();
                if (app instanceof com.quiz.oop.model.app.QuizApp quiz) {
                    quiz.restartQuiz();
                    return true;
                }
            }
            case "quiz_logout" -> {
                App app = phone.getCurrentApp();
                if (app instanceof com.quiz.oop.model.app.QuizApp quiz) {
                    quiz.logout();
                    phone.addNotification("ออกจากระบบ CPE Portal เรียบร้อย");
                    return true;
                }
            }
            case "quiz_select_difficulty" -> {
                App app = phone.getCurrentApp();
                if (app instanceof com.quiz.oop.model.app.QuizApp quiz) {
                    quiz.selectDifficulty(param);
                    return true;
                }
            }
            case "quiz_view_lobby" -> {
                App app = phone.getCurrentApp();
                if (app instanceof com.quiz.oop.model.app.QuizApp quiz) {
                    quiz.openLobby();
                    return true;
                }
            }
            case "quiz_view_leaderboard" -> {
                App app = phone.getCurrentApp();
                if (app instanceof com.quiz.oop.model.app.QuizApp quiz) {
                    quiz.openLeaderboard();
                    return true;
                }
            }
            case "quiz_view_admin" -> {
                App app = phone.getCurrentApp();
                if (app instanceof com.quiz.oop.model.app.QuizApp quiz) {
                    quiz.openAdminPanel();
                    return true;
                }
            }
            case "quiz_add_question" -> {
                App app = phone.getCurrentApp();
                if (app instanceof com.quiz.oop.model.app.QuizApp quiz) {
                    quiz.adminAddQuestion(param);
                    phone.addNotification("Admin เพิ่มข้อสอบใหม่เข้าสู่ระบบสำเร็จ");
                    return true;
                }
            }
            default -> {
                return false;
            }
        }
        return false;
    }
}
