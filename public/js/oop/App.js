/**
 * ============================================================================
 * [OOP - Architecture & Polymorphism สำหรับ Applications]
 * คลาส App และแอปพลิเคชันย่อยบน Android
 * ============================================================================
 */

export class App {
    #id;
    #name;
    #packageName;
    #icon;
    #version;
    #sizeMB;
    #isSystemApp;

    constructor(name, packageName, icon, sizeMB = 25, isSystemApp = false) {
        this.#id = 'APP-' + Math.random().toString(36).substr(2, 7);
        this.#name = name;
        this.#packageName = packageName;
        this.#icon = icon;
        this.#version = "1.0.0";
        this.#sizeMB = sizeMB;
        this.#isSystemApp = isSystemApp;
    }

    get id() { return this.#id; }
    get name() { return this.#name; }
    get packageName() { return this.#packageName; }
    get icon() { return this.#icon; }
    get version() { return this.#version; }
    get sizeMB() { return this.#sizeMB; }
    get isSystemApp() { return this.#isSystemApp; }

    /**
     * เมธอดพื้นฐานสำหรับการทำงานเมื่อเปิดแอป
     */
    launch(phoneContext) {
        return {
            status: "running",
            app: this.#name,
            message: `เปิดแอปพลิเคชัน ${this.#name} (${this.#packageName})`
        };
    }

    /**
     * ดึงข้อมูลสำหรับเรนเดอร์หน้าจอของแอป
     */
    render(phoneContext) {
        return `<div class="app-default-view">
            <h3>${this.#icon} ${this.#name}</h3>
            <p>เวอร์ชัน: ${this.#version} | ขนาด: ${this.#sizeMB} MB</p>
            <p class="text-muted">แอปพลิเคชันทำงานปกติ</p>
        </div>`;
    }
}

/**
 * แอปพลิเคชันกล้องถ่ายรูป (CameraApp)
 */
export class CameraApp extends App {
    constructor() {
        super("กล้อง (Camera)", "com.android.camera", "📷", 45, true);
    }

    render(phoneContext) {
        // phoneContext คือออบเจกต์โทรศัพท์ที่กำลังเปิดแอปนี้อยู่
        return `
        <div class="app-camera-view">
            <div class="camera-lens-view" id="cameraPreview">
                <div class="camera-grid-line"></div>
                <div class="camera-brand-watermark">Powered by ${phoneContext.brand} AI</div>
                <div class="camera-last-shot" id="cameraLastShotPreview"></div>
            </div>
            <div class="camera-controls">
                <div class="camera-mode-tabs">
                    <span class="active">รูปภาพ</span>
                    <span>วิดีโอ</span>
                    <span>บุคคล</span>
                    <span>กลางคืน</span>
                </div>
                <div class="camera-shutter-row">
                    <button class="btn-shutter-filter" id="btnFilterToggle" title="ฟิลเตอร์">✨</button>
                    <button class="btn-camera-shutter" id="btnPhoneShutter" title="กดถ่ายภาพ"></button>
                    <button class="btn-switch-camera" id="btnSwitchCam" title="สลับกล้อง">🔄</button>
                </div>
            </div>
        </div>`;
    }
}

/**
 * แอปพลิเคชันเล่นเพลง (MusicPlayerApp)
 */
export class MusicPlayerApp extends App {
    #isPlaying;
    #currentSongIndex;
    #playlist;

    constructor() {
        super("เพลง (Music)", "com.android.music", "🎵", 32, true);
        this.#isPlaying = false;
        this.#currentSongIndex = 0;
        this.#playlist = [
            { title: "Cyber City Horizon", artist: "Android Synth", duration: "3:42" },
            { title: "Object Oriented Beats", artist: "Code Harmonizer", duration: "4:15" },
            { title: "Polymorphic Melody", artist: "Byte Orchestra", duration: "2:58" },
            { title: "Encapsulated Soul", artist: "The Instances", duration: "3:30" }
        ];
    }

    get isPlaying() { return this.#isPlaying; }
    get currentTrack() { return this.#playlist[this.#currentSongIndex]; }

    togglePlay() {
        this.#isPlaying = !this.#isPlaying;
        return {
            isPlaying: this.#isPlaying,
            track: this.currentTrack
        };
    }

    nextTrack() {
        this.#currentSongIndex = (this.#currentSongIndex + 1) % this.#playlist.length;
        return this.currentTrack;
    }

    prevTrack() {
        this.#currentSongIndex = (this.#currentSongIndex - 1 + this.#playlist.length) % this.#playlist.length;
        return this.currentTrack;
    }

    render(phoneContext) {
        const track = this.currentTrack;
        return `
        <div class="app-music-view">
            <div class="music-disc-wrapper ${this.#isPlaying ? 'spinning' : ''}">
                <div class="music-disc">
                    <div class="music-disc-center">🎧</div>
                </div>
            </div>
            <div class="music-info">
                <h3 id="musicTrackTitle">${track.title}</h3>
                <p id="musicArtistName">${track.artist}</p>
            </div>
            <div class="music-progress-bar">
                <div class="progress-fill" style="width: 45%;"></div>
            </div>
            <div class="music-controls">
                <button class="btn-music-ctrl" id="btnMusicPrev">⏮️</button>
                <button class="btn-music-ctrl play-pause" id="btnMusicPlay">${this.#isPlaying ? '⏸️' : '▶️'}</button>
                <button class="btn-music-ctrl" id="btnMusicNext">⏭️</button>
            </div>
        </div>`;
    }
}

/**
 * แอปพลิเคชันตั้งค่าระบบ (SettingsApp)
 */
export class SettingsApp extends App {
    constructor() {
        super("ตั้งค่า (Settings)", "com.android.settings", "⚙️", 15, true);
    }

    render(phoneContext) {
        const storage = phoneContext.storageInfo;
        const oopHierarchy = phoneContext.getHierarchyChain ? phoneContext.getHierarchyChain() : ["Device", "Smartphone", "AndroidPhone", phoneContext.constructor.name];

        return `
        <div class="app-settings-view">
            <div class="settings-header">
                <h2>การตั้งค่าระบบ</h2>
                <div class="device-badge">${phoneContext.brand} ${phoneContext.model}</div>
            </div>

            <div class="settings-section">
                <div class="section-title">สถานะอุปกรณ์ (Device Status)</div>
                <div class="settings-item">
                    <span>🔋 แบตเตอรี่คงเหลือ</span>
                    <strong>${phoneContext.batteryLevel}% ${phoneContext.isCharging ? '⚡' : ''}</strong>
                </div>
                <div class="settings-item">
                    <span>💾 พื้นที่จัดเก็บข้อมูล</span>
                    <strong>${storage.used} / ${storage.total} GB</strong>
                </div>
                <div class="settings-item">
                    <span>🔓 การล็อกหน้าจอ</span>
                    <strong>ไม่ตั้งรหัสผ่าน (Swipe / None)</strong>
                </div>
            </div>

            <div class="settings-section">
                <div class="section-title">ข้อมูลสถาปัตยกรรม OOP</div>
                <div class="settings-item">
                    <span>คลาสปัจจุบัน (Class)</span>
                    <span class="code-badge">${phoneContext.constructor.name}</span>
                </div>
                <div class="settings-item">
                    <span>ระบบปฏิบัติการ (OS)</span>
                    <span>${phoneContext.androidVersion}</span>
                </div>
                <div class="settings-item">
                    <span>ลำดับการสืบทอด (Inheritance)</span>
                    <span class="code-badge-chain">${oopHierarchy.join(' ➔ ')}</span>
                </div>
            </div>

            <div class="settings-actions">
                <button class="btn-setting-action danger" id="btnResetPhone">🔄 รีเซ็ตการตั้งค่า</button>
            </div>
        </div>`;
    }
}

/**
 * แอปพลิเคชันร้านค้า Google Play Store (PlayStoreApp)
 */
export class PlayStoreApp extends App {
    constructor() {
        super("Play Store", "com.android.vending", "🛍️", 55, true);
    }

    render(phoneContext) {
        const installed = phoneContext.installedAppsList;
        const availableCatalog = [
            { name: "YouTube", pkg: "com.google.android.youtube", icon: "▶️", size: 60, desc: "ดูวิดีโอ เพลง และสตรีมสด" },
            { name: "Instagram", pkg: "com.instagram.android", icon: "📸", size: 48, desc: "แชร์รูปภาพและเรื่องราวของคุณ" },
            { name: "TikTok", pkg: "com.zhiliaoapp.musically", icon: "🎵", size: 75, desc: "วิดีโอสั้นและเอฟเฟกต์สุดล้ำ" },
            { name: "Calculator", pkg: "com.google.android.calculator", icon: "🧮", size: 8, desc: "เครื่องคิดเลขและฟังก์ชันคำนวณ" },
            { name: "Tic Tac Toe", pkg: "com.games.tictactoe", icon: "❌", size: 12, desc: "เกม OX ฝึกสมอง" }
        ];

        return `
        <div class="app-store-view">
            <div class="store-search-bar">
                <span>🔍 ค้นหาแอปพลิเคชัน & เกม...</span>
            </div>
            <div class="store-banner">
                <h3>Google Play Apps</h3>
                <p>OOP Dynamic Object Instantiation</p>
            </div>
            <div class="store-app-list">
                ${availableCatalog.map(app => {
                    const isInstalled = installed.some(inst => inst.packageName === app.pkg);
                    return `
                    <div class="store-app-card">
                        <div class="store-app-icon">${app.icon}</div>
                        <div class="store-app-details">
                            <h4>${app.name}</h4>
                            <p>${app.desc}</p>
                            <small>${app.size} MB</small>
                        </div>
                        <div class="store-app-action">
                            ${isInstalled ? 
                                `<button class="btn-store-uninstall" data-pkg="${app.pkg}">ถอนการติดตั้ง</button>` : 
                                `<button class="btn-store-install" data-pkg="${app.pkg}" data-name="${app.name}" data-icon="${app.icon}" data-size="${app.size}">ติดตั้ง</button>`
                            }
                        </div>
                    </div>`;
                }).join('')}
            </div>
        </div>`;
    }
}

/**
 * แอปพลิเคชันสมุดบันทึก (NotesApp)
 */
export class NotesApp extends App {
    #notes;

    constructor() {
        super("บันทึก (Notes)", "com.android.notes", "📝", 10, true);
        this.#notes = [
            { id: 1, title: "สรุปหลักการ OOP 4 ข้อ", content: "1. Encapsulation\n2. Inheritance\n3. Polymorphism\n4. Abstraction", date: "วันนี้" },
            { id: 2, title: "ข้อดีของการจำลองด้วย OOP", content: "โครงสร้างเป็นระเบียบ นำโค้ดกลับมาใช้ใหม่ได้ง่าย ขยายระบบได้ไม่จำกัด", date: "เมื่อวาน" }
        ];
    }

    addNote(title, content) {
        this.#notes.unshift({
            id: Date.now(),
            title,
            content,
            date: "เมื่อสักครู่"
        });
    }

    render(phoneContext) {
        return `
        <div class="app-notes-view">
            <div class="notes-header">
                <h3>📝 บันทึกข้อความ</h3>
                <button class="btn-add-note" id="btnAddNote">+ บันทึกใหม่</button>
            </div>
            <div class="notes-list">
                ${this.#notes.map(n => `
                    <div class="note-card">
                        <h4>${n.title}</h4>
                        <pre>${n.content}</pre>
                        <span class="note-date">${n.date}</span>
                    </div>
                `).join('')}
            </div>
        </div>`;
    }
}

/**
 * แอปพลิเคชันเกมตอบปัญหาเชาว์ปัญญา (QuizApp)
 */
export class QuizApp extends App {
    constructor() {
        super("เกมตอบปัญหาเชาว์ปัญญา", "com.cpe.quiz", "🧠", 28, true);
    }

    render(phoneContext) {
        return window.renderQuizAppHtml ? window.renderQuizAppHtml() : `<div style='padding:20px; text-align:center;'>กำลังโหลดแอปพลิเคชัน...</div>`;
    }
}

