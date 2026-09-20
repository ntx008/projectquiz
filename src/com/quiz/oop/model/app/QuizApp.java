package com.quiz.oop.model.app;

import com.quiz.oop.model.QuizQuestion;
import com.quiz.oop.model.User;
import java.util.*;

/**
 * ============================================================================
 * [Lecture 3, 4, 5.1 & 5.2: OOP & MVC Application]
 * QuizApp: แอปพลิเคชัน "เกมตอบปัญหาเชาว์ปัญญา"
 * ----------------------------------------------------------------------------
 * - สิทธิ์ Admin: รหัส 6812247008 สามารถเพิ่มคำถาม 3 ระดับ (ง่าย/กลาง/ยาก)
 * - สิทธิ์ Player: ผู้เล่นทั่วไปสามารถเลือกเล่นระดับใดก็ได้ ตอบถูกได้ +1 คะแนน
 * - ระบบสะสมคะแนน และหน้าตารางแรงค์ (Leaderboard)
 * - ธีม Pink & Black Cyber ทันสมัย
 * ============================================================================
 */
public class QuizApp extends App {

    public enum ScreenState {
        AUTH, LOBBY, GAME, RESULT, LEADERBOARD, ADMIN_PANEL
    }

    private final Map<String, User> userDatabase;
    private User currentUser = null;
    private boolean isRegisterTab = false;
    private String authMessage = "";
    private boolean isAuthSuccess = true;
    private String adminMessage = "";

    // Screen navigation
    private ScreenState screenState = ScreenState.AUTH;
    private String currentDifficulty = "easy";

    // Questions organized by difficulty
    private final Map<String, List<QuizQuestion>> questionsByDifficulty;

    // Active quiz session state
    private List<QuizQuestion> activeQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private boolean isAnswered = false;
    private int lastSelectedChoice = -1;

    public QuizApp() {
        super("เกมตอบปัญหาเชาว์ปัญญา", "com.cpe.quiz", "🧩", 28);

        this.userDatabase = new HashMap<>();
        // บัญชี Admin พิเศษ (ตามโจทย์ระบุ รหัส 6812247008) - คะแนนเริ่มต้น 0
        User adminUser = new User(User.ADMIN_ID, "12345678");
        userDatabase.put(User.ADMIN_ID, adminUser);

        // บัญชีผู้เล่นทดสอบตัวอย่าง - คะแนนเริ่มต้น 0
        User demoUser = new User("6812345678", "12345678");
        userDatabase.put("6812345678", demoUser);

        this.questionsByDifficulty = new HashMap<>();
        this.questionsByDifficulty.put("easy", new ArrayList<>());
        this.questionsByDifficulty.put("medium", new ArrayList<>());
        this.questionsByDifficulty.put("hard", new ArrayList<>());
        // ไม่โหลดคำถามตั้งต้น - ให้ Admin รหัส 6812247008 เป็นผู้เพิ่มเองทั้งหมด
    }

    public void addQuestion(String difficulty, QuizQuestion question) {
        String key = (difficulty != null) ? difficulty.toLowerCase() : "easy";
        questionsByDifficulty.computeIfAbsent(key, k -> new ArrayList<>()).add(question);
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    @Override
    public String renderContent() {
        if (!isLoggedIn()) {
            return renderAuthPortal();
        }
        return switch (screenState) {
            case LOBBY -> renderLobbyScreen();
            case GAME -> renderQuizGame();
            case RESULT -> renderResultScreen();
            case LEADERBOARD -> renderLeaderboardScreen();
            case ADMIN_PANEL -> renderAdminPanelScreen();
            default -> renderLobbyScreen();
        };
    }

    /**
     * เรนเดอร์หน้า Login & Register
     */
    private String renderAuthPortal() {
        String activeLoginClass = !isRegisterTab ? "active" : "";
        String activeRegisterClass = isRegisterTab ? "active" : "";
        String btnText = !isRegisterTab ? "🚀 เข้าสู่ระบบ (SIGN IN)" : "✨ ลงทะเบียนบัญชี (SIGN UP)";
        String actionCommand = !isRegisterTab ? "do_login" : "do_register";
        String helperText = !isRegisterTab 
            ? "ยังไม่มีบัญชีใช่หรือไม่? <a href='javascript:void(0)' onclick='triggerEvent(\"quiz_tab\", \"register\")'>สมัครสมาชิกใหม่</a>"
            : "มีบัญชีอยู่แล้วใช่หรือไม่? <a href='javascript:void(0)' onclick='triggerEvent(\"quiz_tab\", \"login\")'>เข้าสู่ระบบที่นี่</a>";

        String registerConfirmField = isRegisterTab ? """
            <div class='modern-form-group'>
                <label class='modern-form-label'><span>🛡️</span> ยืนยันรหัสผ่าน</label>
                <div class='modern-input-box'>
                    <input type='password' id='confirmPasswordInput' class='modern-form-input' placeholder='กรอกรหัสผ่านซ้ำอีกครั้ง...'>
                </div>
            </div>
        """ : "";

        String msgHtml = "";
        if (!authMessage.isEmpty()) {
            String color = isAuthSuccess ? "#ff75b5" : "#f43f5e";
            String icon = isAuthSuccess ? "💖 " : "⚠️ ";
            msgHtml = "<div class='portal-alert-box' style='color: " + color + "; border-color: " + color + "66;'>" + icon + authMessage + "</div>";
        }

        return """
            <div class='custom-portal-card'>
                <div class='portal-brand-banner'>
                    <div class='portal-emblem-icon'>🧠</div>
                    <div class='portal-brand-title'>IQ MASTER PORTAL</div>
                    <div class='portal-brand-subtitle'>ระบบทดสอบเชาว์ปัญญา 3 ระดับ & แผง Admin</div>
                </div>

                <div class='modern-pill-tabs'>
                    <button class='pill-tab-btn %s' onclick='triggerEvent("quiz_tab", "login")'>
                        <span>🔑</span> เข้าสู่ระบบ
                    </button>
                    <button class='pill-tab-btn %s' onclick='triggerEvent("quiz_tab", "register")'>
                        <span>📝</span> ลงทะเบียนใหม่
                    </button>
                </div>

                %s

                <div class='modern-form-group'>
                    <label class='modern-form-label'><span>🆔</span> รหัสนักศึกษา (10 หลัก)</label>
                    <div class='modern-input-box'>
                        <input type='text' id='studentIdInput' class='modern-form-input' placeholder='ตัวอย่าง: 6812247008' maxlength='10'>
                    </div>
                </div>

                <div class='modern-form-group'>
                    <label class='modern-form-label'><span>🔒</span> รหัสผ่านเข้าใช้งาน</label>
                    <div class='modern-input-box'>
                        <input type='password' id='passwordInput' class='modern-form-input' placeholder='กรอกรหัสผ่าน...'>
                    </div>
                </div>

                %s

                <button class='btn-portal-action' onclick='submitAuth("%s")'>%s</button>

                <div class='custom-portal-footer'>
                    %s
                </div>

                <div style='display:flex; flex-direction:column; gap:6px; margin-top:14px;'>
                    <div class='portal-demo-pill' onclick="document.getElementById('studentIdInput').value='6812247008'; document.getElementById('passwordInput').value='12345678';" style="cursor:pointer;" title="คลิกเพื่อเข้าสู่ระบบเป็น Admin">
                        <span>👑 บัญชี Admin: <strong>6812247008</strong> | รหัส: <strong>12345678</strong></span>
                    </div>
                    <div class='portal-demo-pill' onclick="document.getElementById('studentIdInput').value='6812345678'; document.getElementById('passwordInput').value='12345678';" style="cursor:pointer;" title="คลิกเพื่อเข้าสู่ระบบเป็นผู้เล่น">
                        <span>🎮 บัญชีผู้เล่น: <strong>6812345678</strong> | รหัส: <strong>12345678</strong></span>
                    </div>
                </div>
            </div>

            <script>
                function submitAuth(actionType) {
                    const studentId = (document.getElementById('studentIdInput')?.value || '').trim();
                    const password = document.getElementById('passwordInput')?.value || '';
                    const confirmPass = document.getElementById('confirmPasswordInput')?.value || '';
                    if (window.logFirebaseEvent) {
                        window.logFirebaseEvent('auth_' + actionType, { student_id: studentId });
                    }
                    if (actionType === 'register' && window.saveUserToFirestore && studentId.length === 10 && password.length >= 4 && password === confirmPass) {
                        window.saveUserToFirestore(studentId, password);
                    }
                    const param = studentId + '|' + password + '|' + confirmPass;
                    triggerEvent(actionType, param);
                }
            </script>
        """.formatted(activeLoginClass, activeRegisterClass, msgHtml, registerConfirmField, actionCommand, btnText, helperText);
    }

    /**
     * เรนเดอร์หน้าจอ Lobby (เลือกระดับความยาก & เมนูหลัก)
     */
    private String renderLobbyScreen() {
        int easyCount = questionsByDifficulty.getOrDefault("easy", Collections.emptyList()).size();
        int medCount = questionsByDifficulty.getOrDefault("medium", Collections.emptyList()).size();
        int hardCount = questionsByDifficulty.getOrDefault("hard", Collections.emptyList()).size();

        String roleBadge = currentUser.isAdmin() 
            ? "<span class='badge-tag' style='background:linear-gradient(135deg, #f59e0b, #ef4444); color:#fff;'>👑 ADMIN</span>" 
            : "<span class='badge-tag'>🎮 PLAYER</span>";

        String adminBtnHtml = currentUser.isAdmin() ? """
            <button class='btn-portal-action' style='background:linear-gradient(135deg, #f59e0b, #d97706); margin-bottom:10px;' onclick='triggerEvent("quiz_view_admin", "")'>
                ⚙️ แผงควบคุม Admin (เพิ่ม/จัดการข้อสอบ)
            </button>
        """ : "";

        return """
            <div class='custom-portal-card' style='padding: 16px 14px;'>
                <div class='quiz-status-header' style='margin-bottom:12px;'>
                    <div style='display:flex; align-items:center; gap:8px;'>
                        <span style='font-size:13px; font-weight:700; color:#fff0f7;'>👨‍💻 %s</span>
                        %s
                    </div>
                    <button class='btn-quiz-logout' onclick='triggerEvent("quiz_logout", "")' title='ออกจากระบบ'>🚪 ออก</button>
                </div>

                <div style='background:rgba(255,42,133,0.1); border:1px solid rgba(255,42,133,0.3); border-radius:12px; padding:10px 14px; margin-bottom:14px; display:flex; justify-content:space-between; align-items:center;'>
                    <div>
                        <div style='font-size:11px; color:#ff75b5; text-transform:uppercase; letter-spacing:0.5px;'>สถิติคะแนนสะสม</div>
                        <div style='font-size:18px; font-weight:800; color:#fff;'>⭐ %d <span style='font-size:12px; color:#f472b6;'>คะแนน</span></div>
                    </div>
                    <div style='text-align:right;'>
                        <div style='font-size:11px; color:#94a3b8;'>คะแนนสูงสุด</div>
                        <div style='font-size:14px; font-weight:700; color:#38bdf8;'>🏆 %d แต้ม</div>
                    </div>
                </div>

                <div style='font-size:14px; font-weight:700; color:#ff75b5; margin-bottom:10px; display:flex; align-items:center; gap:6px;'>
                    <span>🎯</span> เลือกระดับความยาก (ตอบถูกได้ +1 คะแนน/ข้อ)
                </div>

                <div style='display:flex; flex-direction:column; gap:10px; margin-bottom:14px;'>
                    <!-- ระดับง่าย -->
                    <div onclick='triggerEvent("quiz_select_difficulty", "easy")' style='cursor:pointer; background:linear-gradient(135deg, #181024, #12091c); border:1px solid rgba(34, 197, 94, 0.4); border-radius:12px; padding:12px 14px; display:flex; align-items:center; justify-content:space-between; transition:all 0.2s;'>
                        <div style='display:flex; align-items:center; gap:10px;'>
                            <div style='font-size:24px; background:#14532d; width:40px; height:40px; border-radius:10px; display:flex; align-items:center; justify-content:center;'>🟢</div>
                            <div>
                                <div style='font-size:14px; font-weight:700; color:#4ade80;'>ระดับง่าย (Easy)</div>
                                <div style='font-size:11px; color:#94a3b8;'>โจทย์เชาว์ปัญหากวนๆ เล่นเพลิน</div>
                            </div>
                        </div>
                        <span class='badge-tag' style='background:#15803d;'>%d ข้อ</span>
                    </div>

                    <!-- ระดับกลาง -->
                    <div onclick='triggerEvent("quiz_select_difficulty", "medium")' style='cursor:pointer; background:linear-gradient(135deg, #181024, #12091c); border:1px solid rgba(234, 179, 8, 0.4); border-radius:12px; padding:12px 14px; display:flex; align-items:center; justify-content:space-between; transition:all 0.2s;'>
                        <div style='display:flex; align-items:center; gap:10px;'>
                            <div style='font-size:24px; background:#713f12; width:40px; height:40px; border-radius:10px; display:flex; align-items:center; justify-content:center;'>🟡</div>
                            <div>
                                <div style='font-size:14px; font-weight:700; color:#facc15;'>ระดับกลาง (Medium)</div>
                                <div style='font-size:11px; color:#94a3b8;'>โจทย์ตรรกะและไหวพริบสายวิเคราะห์</div>
                            </div>
                        </div>
                        <span class='badge-tag' style='background:#a16207;'>%d ข้อ</span>
                    </div>

                    <!-- ระดับยาก -->
                    <div onclick='triggerEvent("quiz_select_difficulty", "hard")' style='cursor:pointer; background:linear-gradient(135deg, #181024, #12091c); border:1px solid rgba(239, 68, 68, 0.4); border-radius:12px; padding:12px 14px; display:flex; align-items:center; justify-content:space-between; transition:all 0.2s;'>
                        <div style='display:flex; align-items:center; gap:10px;'>
                            <div style='font-size:24px; background:#7f1d1d; width:40px; height:40px; border-radius:10px; display:flex; align-items:center; justify-content:center;'>🔴</div>
                            <div>
                                <div style='font-size:14px; font-weight:700; color:#f87171;'>ระดับยาก (Hard)</div>
                                <div style='font-size:11px; color:#94a3b8;'>โจทย์เชาว์ขั้นสูง ปริศนาสับขาหลอก</div>
                            </div>
                        </div>
                        <span class='badge-tag' style='background:#b91c1c;'>%d ข้อ</span>
                    </div>
                </div>

                %s

                <button class='btn-portal-action' style='background:linear-gradient(135deg, #a21caf, #ff2a85); margin-bottom:10px;' onclick='triggerEvent("quiz_view_leaderboard", "")'>
                    🏆 ดูตารางแรงค์ (Leaderboard)
                </button>
            </div>
        """.formatted(
            currentUser.getStudentId(),
            roleBadge,
            currentUser.getTotalScore(),
            currentUser.getHighestScore(),
            easyCount,
            medCount,
            hardCount,
            adminBtnHtml
        );
    }

    /**
     * เรนเดอร์หน้าจอทำข้อสอบ
     */
    private String renderQuizGame() {
        if (activeQuestions.isEmpty()) {
            return """
                <div class='custom-portal-card' style='text-align:center; padding:30px 16px;'>
                    <div style='font-size:40px; margin-bottom:10px;'>📭</div>
                    <h3 style='color:#ff75b5; margin-bottom:10px;'>ยังไม่มีข้อสอบในระดับนี้</h3>
                    <p style='color:#94a3b8; font-size:12px; margin-bottom:18px;'>ให้ Admin เข้าไปเพิ่มข้อสอบที่แผงควบคุม</p>
                    <button class='btn-portal-action' onclick='triggerEvent("quiz_view_lobby", "")'>🏠 กลับหน้าหลัก</button>
                </div>
            """;
        }

        QuizQuestion q = activeQuestions.get(currentQuestionIndex);
        StringBuilder choicesHtml = new StringBuilder();

        for (int i = 0; i < q.getChoices().length; i++) {
            String choiceBtnClass = "quiz-choice-btn";
            if (isAnswered) {
                if (i == q.getCorrectIndex()) {
                    choiceBtnClass += " choice-correct";
                } else if (i == lastSelectedChoice) {
                    choiceBtnClass += " choice-wrong";
                }
            }
            String clickAction = !isAnswered ? "onclick='triggerEvent(\"quiz_answer\", \"" + i + "\")'" : "";
            choicesHtml.append(String.format("""
                <button class='%s' %s>%s</button>
            """, choiceBtnClass, clickAction, q.getChoices()[i]));
        }

        String nextBtnHtml = "";
        String explanationHtml = "";
        if (isAnswered) {
            String feedbackColor = (lastSelectedChoice == q.getCorrectIndex()) ? "#ff2a85" : "#f43f5e";
            String feedbackTitle = (lastSelectedChoice == q.getCorrectIndex()) ? "🎉 ยอดเยี่ยม! ตอบถูกต้อง (+1 คะแนน)" : "❌ ตอบผิดซะแล้ว!";
            
            explanationHtml = String.format("""
                <div class='quiz-explanation-box' style='border-left: 3px solid %s;'>
                    <h4 style='color:%s; margin-bottom:4px;'>%s</h4>
                    <p>%s</p>
                </div>
            """, feedbackColor, feedbackColor, feedbackTitle, q.getExplanation());

            nextBtnHtml = """
                <button class='btn-next-question' onclick='triggerEvent("quiz_next", "")'>
                    ข้อถัดไป ➔
                </button>
            """;
        }

        String diffBadge = switch (currentDifficulty) {
            case "easy" -> "<span class='badge-tag' style='background:#166534; color:#4ade80;'>🟢 ระดับง่าย</span>";
            case "medium" -> "<span class='badge-tag' style='background:#854d0e; color:#facc15;'>🟡 ระดับกลาง</span>";
            case "hard" -> "<span class='badge-tag' style='background:#991b1b; color:#f87171;'>🔴 ระดับยาก</span>";
            default -> "<span class='badge-tag'>🧩 คำถาม</span>";
        };

        return """
            <div class='quiz-game-container'>
                <div class='quiz-status-header'>
                    <div class='user-tag'>👨‍💻 %s %s</div>
                    <div class='quiz-score-badge'>คะแนน: %d / %d</div>
                    <button class='btn-quiz-logout' onclick='triggerEvent("quiz_view_lobby", "")' title='กลับหน้าหลัก'>🏠</button>
                </div>

                <div class='quiz-progress-bar-wrap'>
                    <div class='quiz-progress-bar-fill' style='width: %d%%;'></div>
                </div>

                <div class='quiz-question-box'>
                    <span class='q-number-tag'>คำถามข้อที่ %d/%d</span>
                    <h3 class='q-text'>%s</h3>
                </div>

                <div class='quiz-choices-list'>
                    %s
                </div>

                %s

                %s
            </div>
        """.formatted(
            currentUser.getStudentId(),
            diffBadge,
            score, activeQuestions.size(),
            (int)(((currentQuestionIndex + 1) / (double)activeQuestions.size()) * 100),
            (currentQuestionIndex + 1), activeQuestions.size(),
            q.getQuestion(),
            choicesHtml.toString(),
            explanationHtml,
            nextBtnHtml
        );
    }

    /**
     * เรนเดอร์หน้าจอสรุปผลคะแนน
     */
    private String renderResultScreen() {
        String grade = "🥇 อัจฉริยะเชาว์ปัญญาระดับ CPE เทพ";
        if (score <= 1) grade = "🥉 ต้องฝึกคิดนอกกรอบอีกนิดนะ!";
        else if (score <= 2) grade = "🥈 มีไหวพริบยอดเยี่ยม!";

        return """
            <div class='quiz-result-container'>
                <div style='font-size: 46px; margin-bottom: 8px;'>🏆</div>
                <h2>ผลการทดสอบเชาว์ปัญญา</h2>
                <div class='result-student-id'>รหัสนักศึกษา: %s</div>
                
                <div class='result-score-circle'>
                    <div class='result-score-number'>%d</div>
                    <div class='result-score-total'>เต็ม %d คะแนน (+%d แต้ม)</div>
                </div>

                <div class='result-grade-badge'>%s</div>

                <div style='margin-bottom: 14px; font-size:12px; color:#ff75b5;'>
                    คะแนนสะสมรวมทั้งหมด: <strong>%d</strong> คะแนน
                </div>

                <div class='result-actions' style='display:flex; flex-direction:column; gap:8px;'>
                    <button class='btn-restart-quiz' onclick='triggerEvent("quiz_restart", "")'>🔄 เล่นระดับนี้อีกครั้ง</button>
                    <button class='btn-portal-action' style='background:linear-gradient(135deg, #9333ea, #c026d3);' onclick='triggerEvent("quiz_view_leaderboard", "")'>🏆 ดูตารางแรงค์ (Leaderboard)</button>
                    <button class='btn-result-logout' style='background:#1e0d29; border:1px solid #4a154b;' onclick='triggerEvent("quiz_view_lobby", "")'>🏠 กลับหน้าหลัก (Lobby)</button>
                </div>
                <script>
                    if (window.saveQuizScoreToFirestore) {
                        window.saveQuizScoreToFirestore("%s", %d, %d);
                    }
                </script>
            </div>
        """.formatted(
            currentUser.getStudentId(), 
            score, 
            activeQuestions.size(), 
            score,
            grade,
            currentUser.getTotalScore(),
            currentUser.getStudentId(), 
            score, 
            activeQuestions.size()
        );
    }

    /**
     * เรนเดอร์หน้าจอตารางแรงค์ (Leaderboard)
     */
    private String renderLeaderboardScreen() {
        // ดึงผู้เล่นทั้งหมดมาจัดอันดับตามคะแนนสะสม / คะแนนสูงสุด
        List<User> sortedUsers = new ArrayList<>(userDatabase.values());
        sortedUsers.sort((a, b) -> Integer.compare(b.getTotalScore(), a.getTotalScore()));

        StringBuilder rowsHtml = new StringBuilder();
        int rank = 1;
        for (User u : sortedUsers) {
            String medal = switch (rank) {
                case 1 -> "🥇 1";
                case 2 -> "🥈 2";
                case 3 -> "🥉 3";
                default -> " " + rank;
            };
            boolean isMe = currentUser != null && currentUser.getStudentId().equals(u.getStudentId());
            String rowBg = isMe ? "background:rgba(255,42,133,0.18); border-left:3px solid #ff2a85;" : "background:rgba(255,255,255,0.03);";
            String roleTag = u.isAdmin() ? "<span style='color:#f59e0b; font-size:10px;'>👑</span>" : "";

            rowsHtml.append(String.format("""
                <tr style='%s padding:8px 0; border-bottom:1px solid rgba(255,255,255,0.05);'>
                    <td style='padding:8px; font-weight:800; color:%s;'>%s</td>
                    <td style='padding:8px; font-weight:600; color:#fdf2f8;'>%s %s</td>
                    <td style='padding:8px; text-align:center; font-weight:800; color:#ff75b5;'>%d</td>
                    <td style='padding:8px; text-align:center; color:#94a3b8; font-size:11px;'>%d รอบ</td>
                </tr>
            """, rowBg, (rank == 1 ? "#fbbf24" : (rank == 2 ? "#e2e8f0" : (rank == 3 ? "#f97316" : "#94a3b8"))),
            medal, u.getStudentId(), roleTag, u.getTotalScore(), u.getQuizzesPlayed()));
            rank++;
        }

        return """
            <div class='custom-portal-card' style='padding: 16px 14px;'>
                <div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:12px;'>
                    <div style='display:flex; align-items:center; gap:8px;'>
                        <span style='font-size:24px;'>🏆</span>
                        <div>
                            <h3 style='font-size:16px; color:#ff75b5; margin:0;'>ตารางจัดอันดับ (Leaderboard)</h3>
                            <div style='font-size:11px; color:#94a3b8;'>อันดับผู้เล่นคะแนนสูงสุดในระบบ</div>
                        </div>
                    </div>
                    <button class='btn-quiz-logout' onclick='triggerEvent("quiz_view_lobby", "")'>🏠</button>
                </div>

                <div style='overflow-x:auto; margin-bottom:14px; border-radius:8px; border:1px solid rgba(255,42,133,0.2);'>
                    <table style='width:100%; border-collapse:collapse; font-size:12px; text-align:left;'>
                        <thead>
                            <tr style='background:rgba(255,42,133,0.15); color:#ff75b5;'>
                                <th style='padding:8px;'>อันดับ</th>
                                <th style='padding:8px;'>รหัสนักศึกษา</th>
                                <th style='padding:8px; text-align:center;'>คะแนนรวม</th>
                                <th style='padding:8px; text-align:center;'>เล่นแล้ว</th>
                            </tr>
                        </thead>
                        <tbody>
                            %s
                        </tbody>
                    </table>
                </div>

                <button class='btn-portal-action' onclick='triggerEvent("quiz_view_lobby", "")'>
                    🏠 กลับสู่หน้าเลือกโหมด (Lobby)
                </button>
            </div>
        """.formatted(rowsHtml.toString());
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }

    /**
     * เรนเดอร์หน้าจอ Admin Panel (เพิ่มข้อสอบ 3 ระดับ)
     */
    private String renderAdminPanelScreen() {
        if (!currentUser.isAdmin()) {
            return """
                <div class='custom-portal-card' style='text-align:center; padding:30px 16px;'>
                    <div style='font-size:40px; margin-bottom:10px;'>⛔</div>
                    <h3 style='color:#f43f5e;'>ไม่มีสิทธิ์เข้าถึง</h3>
                    <p style='color:#94a3b8; font-size:12px; margin-bottom:18px;'>เฉพาะรหัส 6812247008 เท่านั้นที่สามารถใช้งานแผงนี้ได้</p>
                    <button class='btn-portal-action' onclick='triggerEvent("quiz_view_lobby", "")'>🏠 กลับหน้าหลัก</button>
                </div>
            """;
        }

        String alertHtml = "";
        if (!adminMessage.isEmpty()) {
            alertHtml = "<div class='portal-alert-box' style='color:#4ade80; border-color:#22c55e66; margin-bottom:10px;'>✅ " + adminMessage + "</div>";
        }

        StringBuilder qListHtml = new StringBuilder();
        int totalQs = 0;
        for (String diff : List.of("easy", "medium", "hard")) {
            List<QuizQuestion> qList = questionsByDifficulty.getOrDefault(diff, Collections.emptyList());
            totalQs += qList.size();
            if (!qList.isEmpty()) {
                String badge = switch (diff) {
                    case "easy" -> "🟢 ระดับง่าย (Easy)";
                    case "medium" -> "🟡 ระดับกลาง (Medium)";
                    default -> "🔴 ระดับยาก (Hard)";
                };
                qListHtml.append("<div style='font-size:12px; font-weight:700; color:#ff75b5; margin:12px 0 6px;'>")
                         .append(badge).append(" (").append(qList.size()).append(" ข้อ)</div>");
                for (int i = 0; i < qList.size(); i++) {
                    QuizQuestion q = qList.get(i);
                    String correctText = (q.getCorrectIndex() >= 0 && q.getCorrectIndex() < q.getChoices().length) ? q.getChoices()[q.getCorrectIndex()] : "-";
                    String docId = (q.getId() != null) ? q.getId() : "";
                    String expDiv = (q.getExplanation() != null && !q.getExplanation().isBlank())
                            ? "<div style='font-size:10px; color:#f472b6; margin-top:3px;'>💡 " + escapeHtml(q.getExplanation()) + "</div>"
                            : "";
                    qListHtml.append(String.format("""
                        <div style='background:#18061f; border:1px solid rgba(255,42,133,0.25); border-radius:8px; padding:10px; margin-bottom:8px;'>
                            <div style='display:flex; justify-content:space-between; align-items:flex-start; gap:8px;'>
                                <div style='flex:1;'>
                                    <div style='font-size:12px; font-weight:700; color:#fff0f7; line-height:1.4;'>%d. %s</div>
                                    <div style='font-size:11px; color:#a1a1aa; margin-top:4px;'>
                                        เฉลย: <span style='color:#4ade80; font-weight:600;'>%s</span>
                                    </div>
                                    %s
                                </div>
                                <button onclick='deleteAdminQuestion("%s", %d, "%s")' 
                                    style='background:#3f0c18; border:1px solid #f43f5e; color:#fecdd3; padding:5px 9px; border-radius:6px; font-size:11px; font-weight:700; cursor:pointer; display:flex; align-items:center; gap:4px; white-space:nowrap;'>
                                    🗑️ ลบ
                                </button>
                            </div>
                        </div>
                    """, i + 1, escapeHtml(q.getQuestion()), escapeHtml(correctText), expDiv, diff, i, docId));
                }
            }
        }

        String questionsSection;
        if (totalQs == 0) {
            questionsSection = """
                <div style='text-align:center; padding:16px 10px; background:#14051a; border-radius:10px; border:1px dashed rgba(255,42,133,0.3); color:#94a3b8; font-size:12px;'>
                    📭 ยังไม่มีข้อสอบในระบบ กรุณาเพิ่มข้อสอบด้านบน
                </div>
            """;
        } else {
            questionsSection = qListHtml.toString();
        }

        return """
            <div class='custom-portal-card' style='padding: 16px 14px;'>
                <div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:12px;'>
                    <div style='display:flex; align-items:center; gap:8px;'>
                        <span style='font-size:24px;'>⚙️</span>
                        <div>
                            <h3 style='font-size:15px; color:#f59e0b; margin:0;'>แผงควบคุมผู้ดูแลระบบ (Admin)</h3>
                            <div style='font-size:11px; color:#94a3b8;'>จัดการข้อสอบ 3 ระดับ (รหัส: %s)</div>
                        </div>
                    </div>
                    <button class='btn-quiz-logout' onclick='triggerEvent("quiz_view_lobby", "")'>🏠</button>
                </div>

                %s

                <div class='modern-form-group' style='margin-bottom:8px;'>
                    <label class='modern-form-label'><span>🎯</span> ระดับความยากของคำถาม</label>
                    <select id='adminDiffInput' class='modern-form-input' style='background:#16091c; color:#fff;'>
                        <option value='easy'>🟢 ระดับง่าย (Easy)</option>
                        <option value='medium'>🟡 ระดับกลาง (Medium)</option>
                        <option value='hard'>🔴 ระดับยาก (Hard)</option>
                    </select>
                </div>

                <div class='modern-form-group' style='margin-bottom:8px;'>
                    <label class='modern-form-label'><span>❓</span> คำถามเชาว์ปัญญา</label>
                    <input type='text' id='adminQuestionInput' class='modern-form-input' placeholder='กรอกโจทย์คำถาม...'>
                </div>

                <div style='display:grid; grid-template-columns:1fr 1fr; gap:6px; margin-bottom:8px;'>
                    <div>
                        <label class='modern-form-label'>ตัวเลือก ก.</label>
                        <input type='text' id='adminC0Input' class='modern-form-input' placeholder='ตัวเลือก 1'>
                    </div>
                    <div>
                        <label class='modern-form-label'>ตัวเลือก ข.</label>
                        <input type='text' id='adminC1Input' class='modern-form-input' placeholder='ตัวเลือก 2'>
                    </div>
                    <div>
                        <label class='modern-form-label'>ตัวเลือก ค.</label>
                        <input type='text' id='adminC2Input' class='modern-form-input' placeholder='ตัวเลือก 3'>
                    </div>
                    <div>
                        <label class='modern-form-label'>ตัวเลือก ง.</label>
                        <input type='text' id='adminC3Input' class='modern-form-input' placeholder='ตัวเลือก 4'>
                    </div>
                </div>

                <div class='modern-form-group' style='margin-bottom:8px;'>
                    <label class='modern-form-label'><span>✅</span> ตัวเลือกที่ถูกต้อง</label>
                    <select id='adminCorrectInput' class='modern-form-input' style='background:#16091c; color:#fff;'>
                        <option value='0'>ตัวเลือก ก.</option>
                        <option value='1'>ตัวเลือก ข.</option>
                        <option value='2'>ตัวเลือก ค.</option>
                        <option value='3'>ตัวเลือก ง.</option>
                    </select>
                </div>

                <div class='modern-form-group' style='margin-bottom:12px;'>
                    <label class='modern-form-label'><span>💡</span> คำเฉลย / คำอธิบาย</label>
                    <input type='text' id='adminExpInput' class='modern-form-input' placeholder='กรอกคำอธิบายเฉลย...'>
                </div>

                <button class='btn-portal-action' style='background:linear-gradient(135deg, #f59e0b, #d97706); margin-bottom:14px;' onclick='submitAdminQuestion()'>
                    💾 บันทึกข้อสอบเข้าสู่ระบบ
                </button>

                <div style='margin-top:14px; border-top:1px solid rgba(255,42,133,0.25); padding-top:12px; margin-bottom:12px;'>
                    <div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:8px;'>
                        <div style='font-size:13px; font-weight:700; color:#ff75b5; display:flex; align-items:center; gap:6px;'>
                            <span>📋 รายการข้อสอบทั้งหมด</span>
                            <span style='background:#2a0822; color:#ff2a85; padding:2px 8px; border-radius:10px; font-size:11px; border:1px solid rgba(255,42,133,0.4);'>%d ข้อ</span>
                        </div>
                    </div>
                    %s
                </div>

                <button class='btn-portal-action' style='background:#1e0d29; border:1px solid #4a154b;' onclick='triggerEvent("quiz_view_lobby", "")'>
                    🏠 กลับสู่หน้าเลือกโหมด (Lobby)
                </button>
            </div>

            <script>
                function submitAdminQuestion() {
                    const diff = document.getElementById('adminDiffInput')?.value || 'easy';
                    const q = (document.getElementById('adminQuestionInput')?.value || '').trim();
                    const c0 = (document.getElementById('adminC0Input')?.value || '').trim();
                    const c1 = (document.getElementById('adminC1Input')?.value || '').trim();
                    const c2 = (document.getElementById('adminC2Input')?.value || '').trim();
                    const c3 = (document.getElementById('adminC3Input')?.value || '').trim();
                    const correct = document.getElementById('adminCorrectInput')?.value || '0';
                    const exp = (document.getElementById('adminExpInput')?.value || '').trim();

                    if (!q || !c0 || !c1) {
                        alert('กรุณากรอกคำถามและตัวเลือกอย่างน้อย 2 ข้อ');
                        return;
                    }

                    // บันทึกลง Cloud Firestore ถ้ามี SDK
                    if (window.saveCustomQuestionToFirestore) {
                        window.saveCustomQuestionToFirestore({
                            difficulty: diff,
                            question: q,
                            choices: [c0, c1, c2, c3],
                            correctIndex: parseInt(correct),
                            explanation: exp,
                            createdBy: '6812247008'
                        }).then(res => {
                            const docId = (res && res.id) ? res.id : '';
                            const param = diff + '||' + q + '||' + c0 + '||' + c1 + '||' + c2 + '||' + c3 + '||' + correct + '||' + exp + '||' + docId;
                            triggerEvent('quiz_add_question', param);
                        }).catch(() => {
                            const param = diff + '||' + q + '||' + c0 + '||' + c1 + '||' + c2 + '||' + c3 + '||' + correct + '||' + exp;
                            triggerEvent('quiz_add_question', param);
                        });
                    } else {
                        const param = diff + '||' + q + '||' + c0 + '||' + c1 + '||' + c2 + '||' + c3 + '||' + correct + '||' + exp;
                        triggerEvent('quiz_add_question', param);
                    }
                }

                function deleteAdminQuestion(diff, idx, docId) {
                    if (!confirm('ยืนยันที่จะลบข้อสอบข้อนี้ออกจากระบบหรือไม่?')) return;
                    if (docId && window.deleteQuestionFromFirestore) {
                        window.deleteQuestionFromFirestore(docId);
                    }
                    triggerEvent('quiz_delete_question', diff + '|' + idx);
                }
            </script>
        """.formatted(currentUser.getStudentId(), alertHtml, totalQs, questionsSection);
    }

    @Override
    public String performAction(String actionParam) {
        return "Quiz Action Handled";
    }

    public void setRegisterTab(boolean isRegister) {
        this.isRegisterTab = isRegister;
        this.authMessage = "";
    }

    public boolean login(String studentId, String password) {
        if (studentId == null || studentId.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            this.authMessage = "กรุณากรอกรหัสนักศึกษาและรหัสผ่าน";
            this.isAuthSuccess = false;
            return false;
        }
        String sId = studentId.trim();
        User user = userDatabase.get(sId);
        if (user != null && user.checkPassword(password)) {
            this.currentUser = user;
            this.authMessage = "";
            this.isAuthSuccess = true;
            this.screenState = ScreenState.LOBBY;
            return true;
        } else {
            this.authMessage = "รหัสนักศึกษาหรือรหัสผ่านไม่ถูกต้อง";
            this.isAuthSuccess = false;
            return false;
        }
    }

    public boolean register(String studentId, String password, String confirmPassword) {
        if (studentId == null || studentId.trim().length() != 10) {
            this.authMessage = "รหัสนักศึกษาต้องมีความยาว 10 หลัก";
            this.isAuthSuccess = false;
            return false;
        }
        if (password == null || password.length() < 4) {
            this.authMessage = "รหัสผ่านต้องมีความยาวอย่างน้อย 4 ตัวอักษร";
            this.isAuthSuccess = false;
            return false;
        }
        if (!password.equals(confirmPassword)) {
            this.authMessage = "รหัสผ่านและยืนยันรหัสผ่านไม่ตรงกัน";
            this.isAuthSuccess = false;
            return false;
        }
        String sId = studentId.trim();
        if (userDatabase.containsKey(sId)) {
            this.authMessage = "รหัสนักศึกษานี้ได้ลงทะเบียนไว้แล้ว";
            this.isAuthSuccess = false;
            return false;
        }

        User newUser = new User(sId, password);
        userDatabase.put(sId, newUser);
        this.currentUser = newUser;
        this.authMessage = "สมัครสมาชิกสำเร็จ!";
        this.isAuthSuccess = true;
        this.screenState = ScreenState.LOBBY;
        return true;
    }

    public void selectDifficulty(String difficulty) {
        this.currentDifficulty = (difficulty != null) ? difficulty.toLowerCase() : "easy";
        this.activeQuestions = new ArrayList<>(questionsByDifficulty.getOrDefault(this.currentDifficulty, Collections.emptyList()));
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.isAnswered = false;
        this.lastSelectedChoice = -1;
        this.screenState = ScreenState.GAME;
    }

    public void openLobby() {
        this.screenState = ScreenState.LOBBY;
        this.adminMessage = "";
    }

    public void openLeaderboard() {
        this.screenState = ScreenState.LEADERBOARD;
    }

    public void openAdminPanel() {
        if (currentUser != null && currentUser.isAdmin()) {
            this.screenState = ScreenState.ADMIN_PANEL;
            this.adminMessage = "";
        }
    }

    public void adminAddQuestion(String rawParam) {
        if (currentUser == null || !currentUser.isAdmin()) return;
        try {
            String[] parts = rawParam.split("\\|\\|", -1);
            if (parts.length >= 7) {
                String diff = parts[0];
                String q = parts[1];
                String c0 = parts[2];
                String c1 = parts[3];
                String c2 = parts[4];
                String c3 = parts[5];
                int correct = Integer.parseInt(parts[6]);
                String exp = parts.length > 7 ? parts[7] : "";
                String docId = parts.length > 8 ? parts[8] : "";

                QuizQuestion newQ = new QuizQuestion(
                    q,
                    new String[]{c0, c1, c2, c3},
                    correct,
                    exp,
                    diff
                );
                if (docId != null && !docId.isBlank()) {
                    newQ.setId(docId);
                }
                addQuestion(diff, newQ);
                this.adminMessage = "เพิ่มข้อสอบระดับ " + diff.toUpperCase() + " สำเร็จแล้ว!";
            }
        } catch (Exception e) {
            this.adminMessage = "เกิดข้อผิดพลาดในการเพิ่มข้อสอบ: " + e.getMessage();
        }
    }

    public void adminDeleteQuestion(String difficulty, int index) {
        if (currentUser == null || !currentUser.isAdmin()) return;
        try {
            String diff = (difficulty != null) ? difficulty.toLowerCase() : "easy";
            List<QuizQuestion> list = questionsByDifficulty.get(diff);
            if (list != null && index >= 0 && index < list.size()) {
                QuizQuestion removed = list.remove(index);
                String preview = removed.getQuestion().length() > 25 ? removed.getQuestion().substring(0, 25) + "..." : removed.getQuestion();
                this.adminMessage = "ลบข้อสอบ: \"" + preview + "\" สำเร็จแล้ว!";
            }
        } catch (Exception e) {
            this.adminMessage = "เกิดข้อผิดพลาดในการลบข้อสอบ: " + e.getMessage();
        }
    }

    public void answerQuestion(int choiceIndex) {
        if (isAnswered || activeQuestions.isEmpty()) return;
        this.isAnswered = true;
        this.lastSelectedChoice = choiceIndex;
        QuizQuestion q = activeQuestions.get(currentQuestionIndex);
        if (q.isCorrect(choiceIndex)) {
            this.score++; // ตอบถูกได้ 1 คะแนน
        }
    }

    public void nextQuestion() {
        if (!isAnswered) return;
        this.isAnswered = false;
        this.lastSelectedChoice = -1;
        if (currentQuestionIndex + 1 < activeQuestions.size()) {
            currentQuestionIndex++;
        } else {
            // จบเกมรอบนี้ -> บันทึกคะแนนสะสม
            if (currentUser != null) {
                currentUser.addScore(score);
            }
            this.screenState = ScreenState.RESULT;
        }
    }

    public void restartQuiz() {
        selectDifficulty(this.currentDifficulty);
    }

    public void logout() {
        this.currentUser = null;
        this.authMessage = "";
        this.isRegisterTab = false;
        this.screenState = ScreenState.AUTH;
        this.score = 0;
        this.currentQuestionIndex = 0;
    }
}
