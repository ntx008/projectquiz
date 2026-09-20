package com.quiz.oop.model.app;

import com.quiz.oop.model.QuizQuestion;
import com.quiz.oop.model.User;
import java.util.*;

/**
 * ============================================================================
 * [Lecture 3, 4, 5.1 & 5.2: OOP & MVC Application]
 * QuizApp: แอปพลิเคชัน "เกมตอบปัญหาเชาว์ปัญญา"
 * ----------------------------------------------------------------------------
 * มีหน้าจอ Login และ Register อยู่ด้วยกัน สไตล์ Cyber CPE Portal ตามภาพ
 * มีระบบคำถามเชาว์ปัญญา กวนๆ ชวนคิด ตรวจคะแนนและบันทึกสถิติ
 * ============================================================================
 */
public class QuizApp extends App {
    private final Map<String, User> userDatabase;
    private User currentUser = null;
    private boolean isRegisterTab = false;
    private String authMessage = "";
    private boolean isAuthSuccess = true;

    // Game state
    private final List<QuizQuestion> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private boolean isAnswered = false;
    private int lastSelectedChoice = -1;
    private boolean isGameOver = false;

    public QuizApp() {
        super("เกมตอบปัญหาเชาว์ปัญญา", "com.cpe.quiz", "🧩", 28);

        this.userDatabase = new HashMap<>();
        // บัญชีทดสอบเริ่มต้น (รหัสนักศึกษา 10 หลัก ตามตัวอย่างในภาพ)
        userDatabase.put("6812345678", new User("6812345678", "12345678"));

        this.questions = new ArrayList<>();
        loadQuestions();
    }

    private void loadQuestions() {
        questions.add(new QuizQuestion(
            "อะไรเอ่ย ยิ่งตัด ยิ่งยาว?",
            new String[]{"ก. เล็บมือ", "ข. เส้นผม", "ค. ถนน", "ง. บะหมี่กึ่งสำเร็จรูป"},
            2,
            "💡 เฉลย: ถนน! เพราะถ้าเราตัดถนนเพิ่มขึ้น ความยาวของถนนก็จะยาวขึ้นเรื่อยๆ"
        ));

        questions.add(new QuizQuestion(
            "รถไฟไฟฟ้าแล่นจากทิศเหนือไปทิศใต้ ลมพัดจากทิศตะวันออกไปทิศตะวันตก ควันรถไฟจะพัดไปทางทิศใด?",
            new String[]{"ก. ทิศตะวันตก", "ข. ทิศใต้", "ค. ทิศตะวันออกเฉียงเหนือ", "ง. ไม่มีควัน"},
            3,
            "💡 เฉลย: ไม่มีควัน! เพราะเป็น 'รถไฟไฟฟ้า' จึงไม่มีควันพวยพุ่งออกมา"
        ));

        questions.add(new QuizQuestion(
            "เดือนอะไรมี 30 วัน?",
            new String[]{"ก. เมษายน", "ข. กันยายน", "ค. พฤศจิกายน", "ง. ทุกเดือน (ยกเว้น ก.พ.) มี 30 วัน"},
            3,
            "💡 เฉลย: ทุกเดือนที่มี 31 วัน ก็ย่อมมีวันที่ 30 อยู่ข้างในด้วยเสมอ!"
        ));

        questions.add(new QuizQuestion(
            "อะไรเอ่ย ยิ่งเช็ด ยิ่งเปียก?",
            new String[]{"ก. ผ้าเช็ดตัว", "ข. ทิชชู่เปียก", "ค. แอลกอฮอล์", "ง. ฟองน้ำล้างจาน"},
            0,
            "💡 เฉลย: ผ้าเช็ดตัว! เพราะยิ่งเราใช้เช็ดตัวเราให้แห้ง ผ้าเช็ดตัวก็จะยิ่งเปียกน้ำ"
        ));

        questions.add(new QuizQuestion(
            "มีกระดาษ 1 แผ่น ฉีกครึ่งไปเรื่อยๆ ทั้งหมด 5 ครั้ง จะได้กระดาษกี่ชิ้น?",
            new String[]{"ก. 10 ชิ้น", "ข. 16 ชิ้น", "ค. 25 ชิ้น", "ง. 32 ชิ้น"},
            3,
            "💡 เฉลย: 32 ชิ้น! คำนวณจาก 2^5 = 32 ชิ้น ตามหลักยกกำลังสอง"
        ));

        questions.add(new QuizQuestion(
            "ปลาอะไรเอ่ย ขี้เกียจที่สุดในโลก?",
            new String[]{"ก. ปลาวาฬ", "ข. ปลานิล", "ค. ปลาช่อน", "ง. ปลากระป๋อง"},
            0,
            "💡 เฉลย: ปลาวาฬ! เพราะชอบ 'วาน' (วาฬ) ให้คนอื่นทำแทนตลอดเวลา"
        ));
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    @Override
    public String renderContent() {
        if (!isLoggedIn()) {
            return renderAuthPortal();
        } else if (isGameOver) {
            return renderResultScreen();
        } else {
            return renderQuizGame();
        }
    }

    /**
     * เรนเดอร์หน้า Login & Register ตามสไตล์รูปภาพ CPE PORTAL
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
                    <div class='portal-brand-subtitle'>ระบบทดสอบเชาว์ปัญญาและไหวพริบ</div>
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
                        <input type='text' id='studentIdInput' class='modern-form-input' placeholder='ตัวอย่าง: 6812345678' maxlength='10'>
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

                <div class='portal-demo-pill'>
                    <span>💡 บัญชีทดสอบด่วน: <strong>6812345678</strong> | รหัส: <strong>12345678</strong></span>
                </div>
            </div>

            <script>
                function submitAuth(actionType) {
                    const studentId = document.getElementById('studentIdInput')?.value || '';
                    const password = document.getElementById('passwordInput')?.value || '';
                    const confirmPass = document.getElementById('confirmPasswordInput')?.value || '';
                    if (window.logFirebaseEvent) {
                        window.logFirebaseEvent('auth_' + actionType, { student_id: studentId });
                    }
                    const param = studentId + '|' + password + '|' + confirmPass;
                    triggerEvent(actionType, param);
                }
            </script>
        """.formatted(activeLoginClass, activeRegisterClass, msgHtml, registerConfirmField, actionCommand, btnText, helperText);
    }

    /**
     * เรนเดอร์หน้าจอทำโจทย์เกมตอบปัญหาเชาว์ปัญญา
     */
    private String renderQuizGame() {
        QuizQuestion q = questions.get(currentQuestionIndex);
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
            String feedbackTitle = (lastSelectedChoice == q.getCorrectIndex()) ? "🎉 ยอดเยี่ยม! ตอบถูกต้อง" : "❌ ผิดซะแล้ว!";
            
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

        return """
            <div class='quiz-game-container'>
                <div class='quiz-status-header'>
                    <div class='user-tag'>👨‍💻 %s</div>
                    <div class='quiz-score-badge'>คะแนน: %d / %d</div>
                    <button class='btn-quiz-logout' onclick='triggerEvent("quiz_logout", "")' title='ออกจากระบบ'>🚪 ออก</button>
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
            currentUser.getStudentId(), score, questions.size(),
            (int)(((currentQuestionIndex + 1) / (double)questions.size()) * 100),
            (currentQuestionIndex + 1), questions.size(),
            q.getQuestion(),
            choicesHtml.toString(),
            explanationHtml,
            nextBtnHtml
        );
    }

    /**
     * เรนเดอร์หน้าจอสรุปคะแนนเมื่อเล่นจบ
     */
    private String renderResultScreen() {
        String grade = "🥇 อัจฉริยะเชาว์ปัญญาระดับ CPE เทพ";
        if (score <= 2) grade = "🥉 ต้องฝึกคิดนอกกรอบอีกนิดนะ!";
        else if (score <= 4) grade = "🥈 มีไหวพริบยอดเยี่ยม!";

        return """
            <div class='quiz-result-container'>
                <div style='font-size: 50px; margin-bottom: 12px;'>🏆</div>
                <h2>ผลการทดสอบเชาว์ปัญญา</h2>
                <div class='result-student-id'>รหัสนักศึกษา: %s</div>
                
                <div class='result-score-circle'>
                    <div class='result-score-number'>%d</div>
                    <div class='result-score-total'>เต็ม %d คะแนน</div>
                </div>

                <div class='result-grade-badge'>%s</div>

                <div class='result-actions'>
                    <button class='btn-restart-quiz' onclick='triggerEvent("quiz_restart", "")'>🔄 เล่นอีกครั้ง</button>
                    <button class='btn-result-logout' onclick='triggerEvent("quiz_logout", "")'>🚪 ออกจากระบบ</button>
                </div>
            </div>
        """.formatted(currentUser.getStudentId(), score, questions.size(), grade);
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
        User user = userDatabase.get(studentId.trim());
        if (user != null && user.checkPassword(password)) {
            this.currentUser = user;
            this.authMessage = "";
            this.isAuthSuccess = true;
            this.score = 0;
            this.currentQuestionIndex = 0;
            this.isGameOver = false;
            this.isAnswered = false;
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
        if (userDatabase.containsKey(studentId.trim())) {
            this.authMessage = "รหัสนักศึกษานี้ได้ลงทะเบียนไว้แล้ว";
            this.isAuthSuccess = false;
            return false;
        }

        User newUser = new User(studentId.trim(), password);
        userDatabase.put(studentId.trim(), newUser);
        this.currentUser = newUser;
        this.authMessage = "สมัครสมาชิกสำเร็จ!";
        this.isAuthSuccess = true;
        this.score = 0;
        this.currentQuestionIndex = 0;
        this.isGameOver = false;
        this.isAnswered = false;
        return true;
    }

    public void answerQuestion(int choiceIndex) {
        if (isAnswered || isGameOver) return;
        this.isAnswered = true;
        this.lastSelectedChoice = choiceIndex;
        QuizQuestion q = questions.get(currentQuestionIndex);
        if (q.isCorrect(choiceIndex)) {
            this.score++;
        }
    }

    public void nextQuestion() {
        if (!isAnswered) return;
        this.isAnswered = false;
        this.lastSelectedChoice = -1;
        if (currentQuestionIndex + 1 < questions.size()) {
            this.currentQuestionIndex++;
        } else {
            this.isGameOver = true;
            if (currentUser != null) {
                currentUser.updateScore(score);
            }
        }
    }

    public void restartQuiz() {
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.isAnswered = false;
        this.lastSelectedChoice = -1;
        this.isGameOver = false;
    }

    public void logout() {
        this.currentUser = null;
        this.authMessage = "";
        this.isRegisterTab = false;
        restartQuiz();
    }
}
