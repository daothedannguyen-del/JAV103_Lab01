package com.lab1.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/home/*")
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String pathInfo = req.getPathInfo();
        PrintWriter out = resp.getWriter();

        if (pathInfo == null || pathInfo.equals("/")) {
            out.println("<h3>Welcome to Home Page (default)</h3>");
            return;
        }

        switch (pathInfo) {
            case "/index":
                out.println("<h3>Welcome to Home Page</h3>");
                break;
            case "/about":
                out.println("<h3>About Us Page</h3>");
                break;
            case "/contact":
                out.println("<h3>Contact Page</h3>");
                break;
            default:
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println("<h3>404 - Page not found</h3>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<h3>POST method is called</h3>");
        }
    }
}