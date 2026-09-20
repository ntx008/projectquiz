/**
 * ============================================================================
 * Android Phone UI Simulator Controller
 * ----------------------------------------------------------------------------
 * ทำหน้าที่เชื่อมโยงสถานะของออบเจกต์โทรศัพท์ (OOP Instance) เข้ากับหน้าจอจำลอง
 * และดักจับ Event การกดปุ่ม/แตะหน้าจอเพื่อสั่งงานเมธอดของโทรศัพท์
 * ============================================================================
 */

export class PhoneSimulatorUI {
    constructor(phoneInstance, onLogCallback, onStateChangeCallback) {
        this.phone = phoneInstance;
        this.onLog = onLogCallback || console.log;
        this.onStateChange = onStateChangeCallback || (() => {});
        this.#initDomReferences();
        this.#bindPhysicalButtons();
        this.#bindNavButtons();
        this.#startClockTick();
        this.render();
    }

    /**
     * สลับออบเจกต์โทรศัพท์เครื่องใหม่ (Polymorphism Switcher)
     */
    switchPhone(newPhoneInstance) {
        this.phone = newPhoneInstance;
        this.render();
        this.triggerBootSequence();
    }

    #initDomReferences() {
        this.screenEl = document.getElementById('phoneScreen');
        this.statusBarEl = document.getElementById('phoneStatusBar');
        this.statusTimeEl = document.getElementById('statusTime');
        this.batteryTextEl = document.getElementById('batteryText');
        this.batteryFillEl = document.getElementById('batteryFill');
        this.chargingIconEl = document.getElementById('chargingIcon');
        
        // Screens
        this.bootScreenEl = document.getElementById('bootScreen');
        this.lockScreenEl = document.getElementById('lockScreen');
        this.homeScreenEl = document.getElementById('homeScreen');
        this.appWindowEl = document.getElementById('appWindow');
        this.notificationShadeEl = document.getElementById('notificationShade');
        
        // Lock screen elements
        this.lockTimeEl = document.getElementById('lockTime');
        this.lockDateEl = document.getElementById('lockDate');
        this.pinDotsEl = document.getElementById('pinDots');

        // Home screen elements
        this.homeTimeEl = document.getElementById('homeTime');
        this.homeDateEl = document.getElementById('homeDate');
        this.homeBrandEl = document.getElementById('homeBrandTag');
        this.appGridEl = document.getElementById('appGrid');

        // App window
        this.appTitleEl = document.getElementById('activeAppTitle');
        this.appBodyEl = document.getElementById('activeAppBody');
        this.btnCloseAppEl = document.getElementById('btnCloseApp');

        // Notification shade
        this.notifListEl = document.getElementById('shadeNotifList');
    }

    #bindPhysicalButtons() {
        const btnPower = document.getElementById('btnHwPower');
        const btnVolUp = document.getElementById('btnHwVolUp');
        const btnVolDown = document.getElementById('btnHwVolDown');

        btnPower?.addEventListener('click', () => {
            const res = this.phone.powerButton();
            this.onLog('oop', `phone.powerButton()`, res.message);
            if (this.phone.isPoweredOn) {
                this.triggerBootSequence();
            } else {
                this.render();
            }
            this.onStateChange();
        });

        btnVolUp?.addEventListener('click', () => {
            const res = this.phone.adjustVolume(5);
            this.onLog('info', `phone.adjustVolume(+5)`, res.message);
            this.onStateChange();
        });

        btnVolDown?.addEventListener('click', () => {
            const res = this.phone.adjustVolume(-5);
            this.onLog('info', `phone.adjustVolume(-5)`, res.message);
            this.onStateChange();
        });
    }

    #bindNavButtons() {
        document.getElementById('navBtnBack')?.addEventListener('click', () => {
            if (this.notificationShadeEl.classList.contains('open')) {
                this.toggleNotificationShade(false);
                return;
            }
            if (this.phone.currentApp) {
                const res = this.phone.closeCurrentApp();
                this.onLog('info', `phone.closeCurrentApp()`, res.message);
                this.render();
                this.onStateChange();
            }
        });

        document.getElementById('navBtnHome')?.addEventListener('click', () => {
            this.toggleNotificationShade(false);
            if (this.phone.currentApp) {
                const res = this.phone.closeCurrentApp();
                this.onLog('info', `phone.closeCurrentApp() (Home Button)`, res.message);
            }
            this.render();
            this.onStateChange();
        });

        document.getElementById('navBtnRecents')?.addEventListener('click', () => {
            this.toggleNotificationShade();
        });

        this.btnCloseAppEl?.addEventListener('click', () => {
            const res = this.phone.closeCurrentApp();
            this.onLog('info', `phone.closeCurrentApp()`, res.message);
            this.render();
            this.onStateChange();
        });

        document.getElementById('btnClearNotifs')?.addEventListener('click', () => {
            const res = this.phone.clearNotifications();
            this.onLog('info', `phone.clearNotifications()`, res.message);
            this.renderNotifications();
            this.onStateChange();
        });
    }

    #startClockTick() {
        const updateTime = () => {
            const now = new Date();
            const timeStr = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: false });
            const dateStr = now.toLocaleDateString('th-TH', { weekday: 'long', day: 'numeric', month: 'short' });

            if (this.statusTimeEl) this.statusTimeEl.textContent = timeStr;
            if (this.homeTimeEl) this.homeTimeEl.textContent = timeStr;
            if (this.homeDateEl) this.homeDateEl.textContent = dateStr;
        };
        updateTime();
        setInterval(updateTime, 1000);
    }

    toggleNotificationShade(forceState = null) {
        if (!this.phone.isPoweredOn) return;
        const isOpen = forceState !== null ? forceState : !this.notificationShadeEl.classList.contains('open');
        if (isOpen) {
            this.renderNotifications();
            this.notificationShadeEl.classList.add('open');
        } else {
            this.notificationShadeEl.classList.remove('open');
        }
    }

    triggerBootSequence() {
        if (!this.phone.isPoweredOn) return;
        
        // ดึงการตั้งค่าบูตแบบ Polymorphism
        const bootConfig = this.phone.bootSequence();
        this.onLog('oop', `phone.bootSequence() [Polymorphism]`, 
            `บูตระบบด้วยสไตล์เฉพาะของ ${bootConfig.bootTitle || this.phone.brand}: "${bootConfig.subText}"`);

        const bootLogo = document.getElementById('bootLogoText');
        const bootSub = document.getElementById('bootSubText');
        if (bootLogo) bootLogo.textContent = bootConfig.bootTitle || this.phone.brand;
        if (bootSub) bootSub.textContent = bootConfig.subText || "Powered by Android";

        if (this.bootScreenEl) this.bootScreenEl.style.display = 'flex';
        if (this.homeScreenEl) this.homeScreenEl.style.display = 'none';
        if (this.appWindowEl) this.appWindowEl.classList.remove('active');

        setTimeout(() => {
            if (this.bootScreenEl) this.bootScreenEl.style.display = 'none';
            this.render();
        }, 1200);
    }

    render() {
        // จัดการสถานะเปิด/ปิดเครื่อง
        if (!this.phone.isPoweredOn) {
            this.screenEl.classList.add('screen-off');
            return;
        }
        this.screenEl.classList.remove('screen-off');

        // จัดการ Status Bar (แบตเตอรี่)
        const battery = this.phone.batteryLevel;
        if (this.batteryTextEl) this.batteryTextEl.textContent = `${battery}%`;
        if (this.batteryFillEl) {
            this.batteryFillEl.style.width = `${battery}%`;
            this.batteryFillEl.className = 'battery-level-fill' + 
                (battery <= 20 ? ' low' : (battery <= 50 ? ' medium' : ''));
        }
        
        if (this.chargingIconEl) {
            this.chargingIconEl.style.display = this.phone.isCharging ? 'inline' : 'none';
        }

        // ปรับ Brand Tag บน Home Screen
        if (this.homeBrandEl) {
            this.homeBrandEl.textContent = `${this.phone.brand} ${this.phone.model}`;
        }

        // เข้าสู่หน้าจอหลักโดยตรง (ไม่มีระบบล็อก)
        if (this.homeScreenEl) {
            this.homeScreenEl.style.display = 'flex';
        }

        // เช็คว่ามีแอปพลิเคชันกำลังเปิดอยู่หรือไม่
        if (this.phone.currentApp) {
            this.appWindowEl.classList.add('active');
            this.appTitleEl.textContent = `${this.phone.currentApp.icon} ${this.phone.currentApp.name}`;
            this.appBodyEl.innerHTML = this.phone.currentApp.render(this.phone);
            this.#bindActiveAppControls();
        } else {
            this.appWindowEl.classList.remove('active');
            this.renderHomeApps();
        }
    }

    renderHomeApps() {
        const apps = this.phone.installedAppsList;
        this.appGridEl.innerHTML = apps.map(app => `
            <div class="app-icon-item" data-pkg="${app.packageName}">
                <div class="app-icon-box">${app.icon}</div>
                <span class="app-name-label">${app.name.split(' ')[0]}</span>
            </div>
        `).join('');

        // ผูก Event คลิกไอคอนแอป
        this.appGridEl.querySelectorAll('.app-icon-item').forEach(item => {
            item.addEventListener('click', () => {
                const pkg = item.getAttribute('data-pkg');
                const res = this.phone.launchApp(pkg);
                this.onLog('oop', `phone.launchApp("${pkg}")`, res.message);
                this.render();
                this.onStateChange();
            });
        });
    }

    renderNotifications() {
        const notifs = this.phone.notifications;
        if (notifs.length === 0) {
            this.notifListEl.innerHTML = `<div style="text-align:center; padding: 30px; color:#64748b; font-size:12px;">ไม่มีการแจ้งเตือนใหม่</div>`;
            return;
        }

        this.notifListEl.innerHTML = notifs.map(n => `
            <div class="notif-card">
                <div class="notif-icon">${n.icon}</div>
                <div class="notif-content">
                    <div class="notif-top">
                        <span class="notif-title">${n.title}</span>
                        <span class="notif-time">${n.time}</span>
                    </div>
                    <div class="notif-desc">${n.message}</div>
                </div>
            </div>
        `).join('');
    }

    #bindActiveAppControls() {
        const currentApp = this.phone.currentApp;
        if (!currentApp) return;

        // Camera App Actions
        const btnShutter = document.getElementById('btnPhoneShutter');
        if (btnShutter) {
            btnShutter.addEventListener('click', () => {
                const photoResult = this.phone.takePhoto();
                this.onLog('oop', `phone.takePhoto() [Polymorphic Call]`, 
                    `ถ่ายภาพด้วย ${photoResult.brand}: ${photoResult.feature} (${photoResult.aiProcessing})`);
                
                // Visual Flash Animation
                const preview = document.getElementById('cameraPreview');
                if (preview) {
                    preview.style.backgroundColor = '#fff';
                    setTimeout(() => {
                        preview.style.backgroundColor = '#020617';
                        alert(`📸 [${photoResult.brand}]\nถ่ายภาพสำเร็จ!\nความละเอียด: ${photoResult.resolution}\nฟีเจอร์: ${photoResult.feature}\nประมวลผล: ${photoResult.aiProcessing}`);
                    }, 100);
                }
                this.render();
                this.onStateChange();
            });
        }

        // Music App Actions
        const btnPlay = document.getElementById('btnMusicPlay');
        const btnNext = document.getElementById('btnMusicNext');
        const btnPrev = document.getElementById('btnMusicPrev');
        if (btnPlay) {
            btnPlay.addEventListener('click', () => {
                const res = currentApp.togglePlay();
                this.onLog('info', `musicApp.togglePlay()`, res.isPlaying ? `กำลังเล่น: ${res.track.title}` : 'หยุดเพลง');
                this.render();
            });
        }
        if (btnNext) {
            btnNext.addEventListener('click', () => {
                const track = currentApp.nextTrack();
                this.onLog('info', `musicApp.nextTrack()`, `เปลี่ยนเพลงเป็น: ${track.title}`);
                this.render();
            });
        }
        if (btnPrev) {
            btnPrev.addEventListener('click', () => {
                const track = currentApp.prevTrack();
                this.onLog('info', `musicApp.prevTrack()`, `เปลี่ยนเพลงเป็น: ${track.title}`);
                this.render();
            });
        }

        // Play Store App Actions (Dynamic App Installation)
        const installButtons = document.querySelectorAll('.btn-store-install');
        installButtons.forEach(btn => {
            btn.addEventListener('click', () => {
                const name = btn.getAttribute('data-name');
                const pkg = btn.getAttribute('data-pkg');
                const icon = btn.getAttribute('data-icon');
                const size = parseInt(btn.getAttribute('data-size'));

                import('./App.js').then(({ App }) => {
                    const newApp = new App(name, pkg, icon, size, false);
                    const res = this.phone.installApp(newApp);
                    this.onLog('oop', `phone.installApp(new App("${name}", "${pkg}"))`, res.message);
                    this.render();
                    this.onStateChange();
                });
            });
        });

        const uninstallButtons = document.querySelectorAll('.btn-store-uninstall');
        uninstallButtons.forEach(btn => {
            btn.addEventListener('click', () => {
                const pkg = btn.getAttribute('data-pkg');
                const res = this.phone.uninstallApp(pkg);
                this.onLog('oop', `phone.uninstallApp("${pkg}")`, res.message);
                this.render();
                this.onStateChange();
            });
        });

        // Settings App Actions
        document.getElementById('btnResetPhone')?.addEventListener('click', () => {
            if (confirm("ต้องการรีเซ็ตการตั้งค่าระบบหรือไม่?")) {
                this.phone.clearNotifications();
                this.phone.closeCurrentApp();
                this.phone.lock();
                this.onLog('warn', `phone.resetSettings()`, "รีเซ็ตสถานะหน้าจอและการล็อกเรียบร้อยแล้ว");
                this.render();
                this.onStateChange();
            }
        });
    }
}
