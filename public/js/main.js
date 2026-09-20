/**
 * ============================================================================
 * Main Application & OOP Dashboard Orchestrator
 * ----------------------------------------------------------------------------
 * จัดการแผงควบคุม OOP Inspector, การสร้าง Instance แบบไดนามิก, การทดสอบหลักการ OOP
 * ทั้ง 4 เสาหลัก และการบันทึก Log ลงใน Console
 * ============================================================================
 */

import { SamsungGalaxy, GooglePixel, XiaomiPhone } from './oop/Brands.js';
import { Device } from './oop/Device.js';
import { PhoneSimulatorUI } from './simulator.js';

// ตัวแปรเก็บออบเจกต์โทรศัพท์เครื่องปัจจุบัน
let activePhone = new SamsungGalaxy();
let simulatorUI = null;

// เก็บประวัติ Log
const logContainer = document.getElementById('consoleLogs');

/**
 * ฟังก์ชันบันทึก Log การทำงานของ OOP ลงในหน้าจอ Console
 */
export function addLog(type, caller, message) {
    if (!logContainer) return;

    const time = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
    const logItem = document.createElement('div');
    logItem.className = `log-entry log-${type}`;

    let tagLabel = "INFO";
    if (type === "oop") tagLabel = "OOP CALL";
    if (type === "success") tagLabel = "SUCCESS";
    if (type === "warn") tagLabel = "WARNING";
    if (type === "error") tagLabel = "OOP GUARD";

    logItem.innerHTML = `
        <span class="log-timestamp">[${time}]</span>
        <span class="log-tag">${tagLabel}</span>
        <strong>${caller}:</strong> ${message}
    `;

    logContainer.appendChild(logItem);
    logContainer.scrollTop = logContainer.scrollHeight;
}

/**
 * อัปเดตข้อมูลบนแผง Live Memory / State Inspector
 */
function updateStateInspector() {
    const setSafe = (id, val) => {
        const el = document.getElementById(id);
        if (el) el.textContent = val;
    };

    setSafe('inspectClass', activePhone.constructor.name);
    setSafe('inspectBrandModel', `${activePhone.brand} ${activePhone.model}`);
    setSafe('inspectPower', activePhone.isPoweredOn ? '🟢 เปิด (ON)' : '🔴 ปิด (OFF)');
    setSafe('inspectLock', activePhone.isLocked ? '🔒 ล็อกอยู่' : '🔓 ปลดล็อกแล้ว');
    setSafe('inspectBattery', `${activePhone.batteryLevel}% ${activePhone.isCharging ? '⚡' : ''}`);
    
    const storage = activePhone.storageInfo;
    setSafe('inspectStorage', `${storage.used} / ${storage.total} GB`);
    setSafe('inspectAppsCount', `${activePhone.installedAppsList.length} แอป`);
    setSafe('inspectCurrentApp', activePhone.currentApp ? activePhone.currentApp.name : '(หน้าจอหลัก)');
    
    const chain = activePhone.getHierarchyChain();
    setSafe('inspectHierarchy', chain.join(' ➔ '));
}



/**
 * ผูก Event กับปุ่มสั่งการ OOP Actions
 */
function initActionButtons() {
    // 1. Power Button
    document.getElementById('btnActionPower')?.addEventListener('click', () => {
        const res = activePhone.powerButton();
        addLog('oop', `phone.powerButton()`, res.message);
        if (activePhone.isPoweredOn) {
            simulatorUI.triggerBootSequence();
        } else {
            simulatorUI.render();
        }
        updateStateInspector();
    });

    // 2. Charge Battery
    document.getElementById('btnActionCharge')?.addEventListener('click', () => {
        const res = activePhone.charge(20);
        addLog('oop', `phone.charge(20)`, res.message);
        simulatorUI.render();
        updateStateInspector();
        showToast(res.message);
    });

    // 3. Drain Battery
    document.getElementById('btnActionDrain')?.addEventListener('click', () => {
        const res = activePhone.drainBattery(15);
        addLog('info', `phone.drainBattery(15)`, res.message);
        simulatorUI.render();
        updateStateInspector();
    });

    // 4. Take Photo (Polymorphism Demo)
    document.getElementById('btnActionPhoto')?.addEventListener('click', () => {
        if (!activePhone.isPoweredOn) {
            alert("กรุณาเปิดเครื่องก่อนถ่ายภาพ");
            return;
        }
        const photo = activePhone.takePhoto();
        addLog('oop', `phone.takePhoto() [Polymorphic Call]`, 
            `[${photo.brand}] ${photo.feature} | เลนส์: ${photo.resolution}`);
        showToast(`📸 ${photo.brand}: ${photo.feature}`);
        simulatorUI.render();
        updateStateInspector();
    });

    // 5. Trigger AI Assistant (Polymorphism Demo)
    document.getElementById('btnActionAssistant')?.addEventListener('click', () => {
        if (!activePhone.isPoweredOn) {
            alert("กรุณาเปิดเครื่องก่อนเรียกผู้ช่วย AI");
            return;
        }
        const ai = activePhone.triggerAssistant();
        addLog('oop', `phone.triggerAssistant() [Polymorphic Call]`, 
            `เรียกใช้งาน ${ai.assistantName}: "${ai.greeting}"`);
        activePhone.pushNotification(ai.assistantName, ai.greeting, "🤖");
        simulatorUI.renderNotifications();
        showToast(`🤖 ${ai.assistantName}: ${ai.greeting}`);
    });

    // 6. Push Notification
    document.getElementById('btnActionNotif')?.addEventListener('click', () => {
        const sampleNotifs = [
            { title: "LINE", msg: "คุณได้รับข้อความใหม่จาก อาจารย์วิชา OOP", icon: "💬" },
            { title: "Shopee", msg: "พัสดุของคุณเตรียมจัดส่งแล้ววันนี้!", icon: "📦" },
            { title: "GitHub", msg: "New Pull Request: Merge oop-features into main", icon: "🐙" },
            { title: "Google Calendar", msg: "การสอบ Final วิชา OOP จะเริ่มใน 1 ชั่วโมง", icon: "📅" }
        ];
        const randomNotif = sampleNotifs[Math.floor(Math.random() * sampleNotifs.length)];
        const notif = activePhone.pushNotification(randomNotif.title, randomNotif.msg, randomNotif.icon);
        addLog('info', `phone.pushNotification("${randomNotif.title}", ...)`, `ส่งการแจ้งเตือนสำเร็จ`);
        simulatorUI.renderNotifications();
        showToast(`${randomNotif.icon} ${randomNotif.title}: ${randomNotif.msg}`);
    });

    // 7. Special Brand Feature Button
    document.getElementById('btnActionSpecial')?.addEventListener('click', () => {
        if (!activePhone.isPoweredOn) {
            alert("กรุณาเปิดเครื่องก่อนใช้งานฟีเจอร์นี้");
            return;
        }

        if (activePhone instanceof SamsungGalaxy) {
            const res = activePhone.activateSPen();
            addLog('oop', `activePhone.activateSPen()`, res.message);
            showToast(res.message);
        } else if (activePhone instanceof GooglePixel) {
            const res = activePhone.magicEraser();
            addLog('oop', `activePhone.magicEraser()`, res.message);
            showToast(res.message);
        } else if (activePhone instanceof XiaomiPhone) {
            const res = activePhone.charge(30);
            addLog('oop', `activePhone.charge(30) [120W HyperCharge Override]`, res.message);
            simulatorUI.render();
            updateStateInspector();
            showToast(res.message);
        }
    });

    // 8. Encapsulation Test (พยายามแอบแก้ไข Private Field ตรงๆ)
    document.getElementById('btnTestEncapsulation')?.addEventListener('click', () => {
        addLog('warn', `TRY ILLEGAL ACCESS: phone.#batteryLevel = 100`, 
            "กำลังทดสอบพยายามเขียนทับ Private Property โดยตรง...");
        
        try {
            // ใน JavaScript Private Field # ไม่สามารถเข้าถึงจากภายนอก Class ได้เลย
            // เราจำลองการทดสอบโดยพยายามเข้าถึง property ตรงๆ
            eval("activePhone.#batteryLevel = 100;");
        } catch (err) {
            addLog('error', `🛡️ [Encapsulation Guard Active]`, 
                `ถูกสกัดกั้นสำเร็จ! JavaScript ปฏิเสธการเข้าถึง: "${err.message}". ข้อมูลแบตเตอรี่ปลอดภัย ไม่สามารถถูกแทรกแซงจากภายนอกได้`);
            alert(`🛡️ [OOP Encapsulation ทำงานสมบูรณ์!]\n\nระบบปฏิเสธการเข้าถึง:\n${err.message}\n\nคุณสมบัติ #batteryLevel ถูกห่อหุ้มและซ่อนสถานะไว้ภายใน ต้องเปลี่ยนแปลงผ่านเมธอด charge() หรือ drainBattery() เท่านั้น!`);
        }
    });

    // 9. Abstraction Test (พยายาม new Device ตรงๆ)
    document.getElementById('btnTestAbstraction')?.addEventListener('click', () => {
        addLog('warn', `TRY ILLEGAL INSTANTIATION: new Device()`, 
            "กำลังทดสอบสร้าง Instance จาก Abstract Class Device โดยตรง...");
        try {
            new Device("Generic", "Prototype");
        } catch (err) {
            addLog('error', `🛡️ [Abstraction Contract Active]`, 
                `ถูกบล็อกสำเร็จ! ${err.message}`);
            alert(`🛡️ [OOP Abstraction ทำงานสมบูรณ์!]\n\n${err.message}\n\nคลาส Device เป็นโครงสร้างแม่แบบระดับสูง ไม่อนุญาตให้สร้างเป็นตัวเครื่องตรงๆ ต้องสืบทอด (Inherit) เป็น Smartphone หรือ AndroidPhone ก่อนเสมอ`);
        }
    });

    // Clear Logs Button
    document.getElementById('btnClearLogs')?.addEventListener('click', () => {
        if (logContainer) logContainer.innerHTML = '';
        addLog('info', 'Console', 'ล้างประวัติการบันทึกเรียบร้อย');
    });
}

/**
 * แท็บสลับหน้าจอ (Workbench Tabs)
 */
function initTabs() {
    const tabButtons = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');

    tabButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const target = btn.getAttribute('data-tab');
            tabButtons.forEach(b => b.classList.remove('active'));
            tabContents.forEach(c => c.classList.remove('active'));

            btn.classList.add('active');
            document.getElementById(target)?.classList.add('active');
        });
    });
}

/**
 * กล่องข้อความ Toast แจ้งเตือนมุมจอ
 */
function showToast(message) {
    const container = document.getElementById('toastContainer');
    if (!container) return;

    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.textContent = message;
    container.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateY(10px)';
        toast.style.transition = 'all 0.3s ease';
        setTimeout(() => toast.remove(), 300);
    }, 3200);
}

// เริ่มต้นระบบเมื่อโหลดหน้าเว็บเสร็จสมบูรณ์
window.addEventListener('DOMContentLoaded', () => {
    // เปิดเครื่องครั้งแรก
    activePhone.powerButton();

    // สร้าง UI Simulator
    simulatorUI = new PhoneSimulatorUI(
        activePhone,
        (type, caller, msg) => addLog(type, caller, msg),
        () => updateStateInspector()
    );

    initActionButtons();
    initTabs();
    updateStateInspector();

    addLog('info', 'System Boot', 'เริ่มต้นระบบจำลอง Android Phone OOP Simulator สำเร็จ');
    addLog('oop', 'activePhone = new SamsungGalaxy()', 'สร้างออบเจกต์ Samsung Galaxy S24 Ultra เริ่มต้น (สืบทอด 4 ลำดับขั้น)');

    simulatorUI.triggerBootSequence();
});
