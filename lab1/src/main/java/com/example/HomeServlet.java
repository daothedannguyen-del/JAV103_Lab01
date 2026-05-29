package com.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// @WebServlet là để ánh xạ URL vào Servlet này
@WebServlet(urlPatterns = {"/home/index", "/home/about", "/home/contact"})
public class HomeServlet extends HttpServlet {

    // Xử lý khi người dùng gửi request bằng GET
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        // Thiết lập kiểu trả về là HTML
        response.setContentType("text/html;charset=UTF-8");
        
        // Lấy đối tượng để ghi HTML ra trình duyệt
        PrintWriter out = response.getWriter();
        
        // Lấy URL path (ví dụ: /home/index)
        String path = request.getServletPath();
        
        // Kiểm tra path và hiển thị nội dung tương ứng
        if (path.equals("/home/index")) {
            out.println("<h1>Welcome to Home Page</h1>");
        } else if (path.equals("/home/about")) {
            out.println("<h1>About Us Page</h1>");
        } else if (path.equals("/home/contact")) {
            out.println("<h1>Contact Page</h1>");
        }
    }
    
    // Xử lý khi người dùng gửi request bằng POST
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<h1>POST method is called</h1>");
    }
}

