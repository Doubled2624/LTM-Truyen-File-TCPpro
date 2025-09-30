# 🎓 Faculty of Information Technology — DaiNam University  
## 📡 Ứng dụng Chat & Truyền File TCP  

<div align="center">
  <img src="docs/aiotlab_logo.png" alt="AIoTLab Logo" width="150"/>
  <img src="docs/fitdnu_logo.png" alt="FIT DNU Logo" width="160"/>
  <img src="docs/dnu_logo.png" alt="DaiNam University Logo" width="180"/>
</div>

<div align="center">

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of IT](https://img.shields.io/badge/Faculty%20of%20IT-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>

---

## 📖 Giới thiệu  
Ứng dụng **Chat & Truyền File Client–Server** viết bằng **Java** sử dụng **TCP Socket**.  
Cho phép nhiều người dùng **giao tiếp thời gian thực** và **chia sẻ file** qua mạng LAN/Internet.

- **Server**: quản lý kết nối, phân phối tin nhắn và file.  
- **Client**: giao diện Java Swing để chat và gửi/nhận file.  
- **Lưu trữ**: lịch sử chat và log lưu vào file văn bản.  

---

## ⚙️ Chức năng  

### 🖥️ Server
- Quản lý kết nối đa luồng.
- Phân phối tin nhắn & file cho các client.
- Lưu log kèm timestamp.
- Ngắt kết nối và xử lý lỗi.

### 💻 Client
- Kết nối qua IP + Port.
- Gửi/nhận **tin nhắn**.
- Gửi/nhận **file** (có progress bar).
- Giao diện Swing (chat box, chọn file, trạng thái kết nối).
- Hiển thị thông báo khi mất kết nối hoặc lỗi.

---

## 🛠️ Công nghệ  
- **Java Core (JDK 8+)**  
- **Multithreading**  
- **Java Swing (GUI)**  
- **TCP Socket (ServerSocket, Socket)**  
- **File I/O (ghi log, lưu file)**  

---

## 🖼️ Demo giao diện  

- Giao diện **Server**  
  ![Server](image.png)

- Giao diện **Client**  
  ![Client](image-1.png)

- Client gửi file  
  ![Send File](image-2.png)

- Server nhận file  
  ![Receive File](image-3.png)

---

## 📦 Cấu trúc thư mục  

/src
/server
ServerMain.java
ClientHandler.java
ServerWindow.java
Protocol.java
/client
ClientMain.java
ChatWindow.java
FileSender.java
FileReceiver.java
Protocol.java
/docs
aiotlab_logo.png
fitdnu_logo.png
dnu_logo.png
image.png
image-1.png
image-2.png
image-3.png

yaml
Sao chép mã

---

## 🚀 Cài đặt & Chạy  

### 1. Yêu cầu
- JDK 8 trở lên (khuyến nghị JDK 17/21)  
- IDE (Eclipse, IntelliJ, VS Code) hoặc dòng lệnh  

### 2. Clone project
```bash
git clone https://github.com/<your-username>/<your-repo>.git
cd <your-repo>
3. Chạy bằng IDE
Import project

Chạy ServerMain trước (mặc định cổng 5555)

Chạy ClientMain, nhập IP + Port để kết nối

4. Chạy bằng Terminal
bash
Sao chép mã
javac -d out src/server/*.java src/client/*.java
java -cp out server.ServerMain 5555
java -cp out client.ClientMain 127.0.0.1 5555
5. Truyền file
Client chọn 📎 Send File → chọn file → progress hiển thị.

Người nhận có thể Accept / Decline lưu file.

❗ Lỗi thường gặp
Connection refused → Server chưa chạy hoặc sai port.

Address already in use → Port đang bận → đổi port khác.

Firewall chặn → Cho phép Java qua tường lửa.

Kết nối khác mạng → dùng IP LAN thực, mở port router nếu cần.

👤 Thông tin liên hệ
Họ tên: Bùi Tuấn Dương

Lớp: CNTT 16-03

Email: buibanh2k4@gmail.com

© 2025 AIoTLab, Faculty of IT, DaiNam University.