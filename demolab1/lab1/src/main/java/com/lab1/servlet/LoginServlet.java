package com.lab1.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login/*")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if ("/form".equals(pathInfo)) {
            showLoginForm(resp, req.getContextPath());
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showLoginForm(HttpServletResponse resp, String contextPath) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Login Form</title></head><body>");
        out.println("<h2>Đăng nhập</h2>");
        out.println("<form action=\"" + contextPath + "/login/check\" method=\"post\">");
        out.println("Username: <input type='text' name='username' required/><br/><br/>");
        out.println("Password: <input type='password' name='password' required/><br/><br/>");
        out.println("Gender: <input type='radio' name='gender' value='Male'/>Male ");
        out.println("<input type='radio' name='gender' value='Female'/>Female<br/><br/>");
        out.println("Hobbies: <input type='checkbox' name='hobbies' value='Reading'/>Reading ");
        out.println("<input type='checkbox' name='hobbies' value='Music'/>Music ");
        out.println("<input type='checkbox' name='hobbies' value='Sports'/>Sports<br/><br/>");
        out.println("City: <select name='city'>");
        out.println("<option value='Hà Nội'>Hà Nội</option>");
        out.println("<option value='Đà Nẵng'>Đà Nẵng</option>");
        out.println("<option value='Hồ Chí Minh'>Hồ Chí Minh</option>");
        out.println("</select><br/><br/>");
        out.println("<input type='submit' value='Login'/>");
        out.println("</form></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if ("/check".equals(pathInfo)) {
            processLogin(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void processLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String gender = req.getParameter("gender");
        String[] hobbies = req.getParameterValues("hobbies");
        String city = req.getParameter("city");

        if (!"123".equals(password)) {
            out.println("<h3 style='color:red'>Lỗi: Mật khẩu không đúng!</h3>");
            out.println("<a href='" + req.getContextPath() + "/login/form'>Quay lại form</a>");
            return;
        }

        out.println("<h2>Thông tin đăng nhập</h2>");
        out.println("<table border='1' cellpadding='5'>");
        out.println("<tr><th>Field</th><th>Value</th></tr>");
        out.println("<tr><td>Username</td><td>" + escapeHtml(username) + "</td></tr>");
        out.println("<tr><td>Password</td><td>" + escapeHtml(password) + "</td></tr>");
        out.println("<tr><td>Gender</td><td>" + escapeHtml(gender) + "</td></tr>");
        out.print("<tr><td>Hobbies</td><td>");
        if (hobbies == null || hobbies.length == 0) out.print("None");
        else {
            for (int i = 0; i < hobbies.length; i++) {
                if (i > 0) out.print(", ");
                out.print(escapeHtml(hobbies[i]));
            }
        }
        out.println("</td></tr>");
        out.println("<tr><td>City</td><td>" + escapeHtml(city) + "</td></tr>");
        out.println("</table>");
        out.println("<br/><a href='" + req.getContextPath() + "/login/form'>Đăng nhập lại</a>");
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}