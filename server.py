"""
============================================================================
Zero-dependency Python HTTP Server
----------------------------------------------------------------------------
ทางเลือกสำรอง: รันเซิร์ฟเวอร์ด้วยภาษา Python (พอร์ต 3000)
คำสั่ง: python server.py
============================================================================
"""

import http.server
import socketserver
import os

PORT = 3000
PUBLIC_DIR = os.path.join(os.path.dirname(__file__), 'public')

class CustomHandler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=PUBLIC_DIR, **kwargs)

    def end_headers(self):
        # เพิ่ม headers ป้องกันแคชขณะพัฒนา
        self.send_header('Cache-Control', 'no-cache, no-store, must-revalidate')
        super().end_headers()

if __name__ == '__main__':
    with socketserver.TCPServer(("", PORT), CustomHandler) as httpd:
        print(f"\n======================================================")
        print(f"🤖 [Python] Android OOP Simulator รันบน Localhost แล้ว!")
        print(f"🌐 เปิดดูในเบราว์เซอร์ที่: http://localhost:{PORT}")
        print(f"======================================================\n")
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\nปิดการทำงานของเซิร์ฟเวอร์เรียบร้อย")
