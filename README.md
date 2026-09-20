# 📱 Android Smartphone OOP Simulator (100% Pure Java Edition)

โปรเจกต์จำลองการทำงานของสมาร์ตโฟน Android พัฒนาด้วย **ภาษา Java ล้วน (100% Pure Java)** สอดคล้องกับเนื้อหาบทเรียนวิชาการเขียนโปรแกรมเชิงวัตถุและการพัฒนาแอปพลิเคชัน (Lectures 0 ถึง 6) พร้อมหน้าจอแสดงผลเสมือนจริงบน Localhost

---

## 📚 ความสอดคล้องกับบทเรียนในรายวิชา (Course Syllabus Mapping)

| บทเรียน | หัวข้อบทเรียน | การประยุกต์ใช้ในโปรเจกต์ Java นี้ | คลาส / ไฟล์ที่เกี่ยวข้อง |
| :--- | :--- | :--- | :--- |
| **Lecture 1** | **Android Studio and Github** | ใช้ Java OpenJDK 21 (ที่มาพร้อมกับ Android Studio JBR) ในการคอมไพล์และรันระบบ | [`run.bat`](file:///d:/0500/projectquiz/run.bat), [`run.ps1`](file:///d:/0500/projectquiz/run.ps1) |
| **Lecture 2** | **Coding in Java** | การเขียนไวยากรณ์ภาษา Java ตามมาตรฐาน, การแบ่ง Package, Type Safety, Control Structures | [`Main.java`](file:///d:/0500/projectquiz/src/com/quiz/oop/Main.java) |
| **Lecture 3** | **OOP class and object** | การสร้างคลาส, Instance Variables, Constructor, การใช้ `this`, การ instantiate ออบเจกต์ | [`Device.java`](file:///d:/0500/projectquiz/src/com/quiz/oop/model/Device.java), [`Smartphone.java`](file:///d:/0500/projectquiz/src/com/quiz/oop/model/Smartphone.java) |
| **Lecture 4** | **OOP concept** | ครบทั้ง 4 เสาหลัก:<br>1. **Encapsulation**: ซ่อนข้อมูลสำคัญด้วย `private` (เช่น แบตเตอรี่ 100% คงที่ไม่มีวันลด)<br>2. **Inheritance**: ลำดับขั้น `Device` $\rightarrow$ `Smartphone` $\rightarrow$ `AndroidPhone`<br>3. **Polymorphism**: การ `@Override` เมธอด `renderContent()`, `bootSequence()` ในแต่ละแอป<br>4. **Abstraction**: คลาส `Device` เป็น `abstract class` และอินเตอร์เฟซ `Powerable` | [`Powerable.java`](file:///d:/0500/projectquiz/src/com/quiz/oop/model/Powerable.java), [`AndroidPhone.java`](file:///d:/0500/projectquiz/src/com/quiz/oop/model/AndroidPhone.java), [`App.java`](file:///d:/0500/projectquiz/src/com/quiz/oop/model/app/App.java) |
| **Lecture 5.1**| **Event-driven Programming** | สถาปัตยกรรม Event Bus: การสร้าง `PhoneEvent`, การกำหนด Listener Interface `PhoneEventListener` และคลาสกระจายเหตุการณ์ `EventDispatcher` | [`PhoneEvent.java`](file:///d:/0500/projectquiz/src/com/quiz/oop/event/PhoneEvent.java), [`EventDispatcher.java`](file:///d:/0500/projectquiz/src/com/quiz/oop/event/EventDispatcher.java) |
| **Lecture 5.2**| **MVC Architectural Pattern** | แบ่งสถาปัตยกรรมออกเป็น 3 ส่วนชัดเจน:<br>- **Model**: จัดการข้อมูลโทรศัพท์และแอป (`AndroidPhone`, `App`)<br>- **View**: สร้าง UI แสดงผลหน้าจอมือถือบนเว็บ (`PhoneWebView`)<br>- **Controller**: รับคำสั่งจากผู้ใช้ ประมวลผลและอัปเดต Model (`PhoneController`) | [`PhoneController.java`](file:///d:/0500/projectquiz/src/com/quiz/oop/controller/PhoneController.java), [`PhoneWebView.java`](file:///d:/0500/projectquiz/src/com/quiz/oop/view/PhoneWebView.java) |
| **Lecture 6** | **Multitasking** | การทำงานแบบ Multithreading เบื้องหลังโดยใช้ `Runnable`, `Thread` และ `ExecutorService` (เช่น นาฬิกาที่อัปเดตแบบเรียลไทม์โดยไม่บล็อกการทำงาน) | [`ClockTask.java`](file:///d:/0500/projectquiz/src/com/quiz/oop/multitasking/ClockTask.java), [`MultitaskManager.java`](file:///d:/0500/projectquiz/src/com/quiz/oop/multitasking/MultitaskManager.java) |

---

## 📱 คุณสมบัติตามข้อกำหนดของผู้ใช้

1. **โทรศัพท์เพียงเครื่องเดียว (Single Phone)**:
   - แสดงผลสมาร์ตโฟน Android เครื่องเดียวตรงกลางหน้าจอ ไม่มีความซับซ้อน ไม่มีแถบเลือก
2. **แบตเตอรี่ไม่มีวันลด (Infinite 100% Battery)**:
   - คลาส `Smartphone.java` กำหนดให้ตัวแปร Encapsulated `#batteryLevel = 100` ตลอดกาล
   - เมื่อมีการใช้งานแอป, ถ่ายภาพ, เล่นเพลง หรือเรียกใช้เมธอด `drainBattery()` แบตเตอรี่ยังคงอยู่ที่ **100% เสมอ**
3. **ไม่มีระบบใส่รหัสผ่าน (No Passcode / PIN)**:
   - เข้าใช้งานหน้าจอหลัก (Home Screen) ได้ทันทีเมื่อเปิดเครื่อง

---

## 🚀 วิธีการคอมไพล์และรันโปรแกรม

### วิธีที่ 1: ดับเบิลคลิกไฟล์ `run.bat`
เพียงดับเบิลคลิกไฟล์ [`run.bat`](file:///d:/0500/projectquiz/run.bat) หรือรันผ่าน Command Prompt:
```cmd
run.bat
```

### วิธีที่ 2: รันผ่าน PowerShell
```powershell
.\run.ps1
```

### วิธีที่ 3: คอมไพล์ด้วยคำสั่ง javac / java โดยตรง
```bash
& "C:\Program Files\Android\Android Studio\jbr\bin\javac.exe" -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src | Select-Object -ExpandProperty FullName)
& "C:\Program Files\Android\Android Studio\jbr\bin\java.exe" -cp out com.quiz.oop.Main
```

เมื่อเซิร์ฟเวอร์เริ่มทำงานแล้ว ให้เปิดเบราว์เซอร์ไปที่:
👉 **[http://localhost:3000](http://localhost:3000)**
