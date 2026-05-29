package com.lab1.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lab1.model.Product;

@WebServlet("/product/*")
public class ProductServlet extends HttpServlet {

    private final List<Product> productList = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @Override
    public void init() throws ServletException {
        productList.add(new Product(idCounter.getAndIncrement(), "Laptop", 1500.0));
        productList.add(new Product(idCounter.getAndIncrement(), "Mouse", 25.5));
        productList.add(new Product(idCounter.getAndIncrement(), "Keyboard", 45.0));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("text/html;charset=UTF-8");

        if (pathInfo == null) pathInfo = "/";

        switch (pathInfo) {
            case "/index":
                showProductList(resp, req.getContextPath());
                break;
            case "/create":
                showCreateForm(resp, req.getContextPath());
                break;
            default:
                if (pathInfo.startsWith("/edit/")) {
                    String idStr = pathInfo.substring(6);
                    showEditForm(resp, idStr, req.getContextPath());
                } else if (pathInfo.startsWith("/delete/")) {
                    String idStr = pathInfo.substring(8);
                    deleteProduct(resp, idStr, req.getContextPath());
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
        }
    }

    private void showProductList(HttpServletResponse resp, String contextPath) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<h2>Danh sách sản phẩm</h2>");
        out.println("<table border='1' cellpadding='5'>");
        out.println("<tr><th>ID</th><th>Tên</th><th>Giá</th><th>Hành động</th></tr>");
        for (Product p : productList) {
            out.printf("<tr>"
                            + "<td>%d</td><td>%s</td><td>%.2f</td>"
                            + "<td><a href='%s/product/edit/%d'>Sửa</a> | "
                            + "<a href='%s/product/delete/%d' onclick='return confirm(\"Xóa?\")'>Xóa</a></td>"
                            + "</tr>",
                    p.getId(), escapeHtml(p.getName()), p.getPrice(),
                    contextPath, p.getId(),
                    contextPath, p.getId());
        }
        out.println("</table>");
        out.println("<br/><a href='" + contextPath + "/product/create'>Thêm sản phẩm mới</a>");
        out.println("&nbsp;|&nbsp;<a href='" + contextPath + "/home/index'>Về trang chủ</a>");
    }

    private void showCreateForm(HttpServletResponse resp, String contextPath) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<h2>Thêm sản phẩm</h2>");
        out.println("<form method='post' action='" + contextPath + "/product/create'>");
        out.println("Tên: <input type='text' name='name' required/><br/><br/>");
        out.println("Giá: <input type='number' step='0.01' name='price' required/><br/><br/>");
        out.println("<input type='submit' value='Tạo'/>");
        out.println("</form>");
        out.println("<a href='" + contextPath + "/product/index'>Quay lại danh sách</a>");
    }

    private void showEditForm(HttpServletResponse resp, String idStr, String contextPath) throws IOException {
        PrintWriter out = resp.getWriter();
        try {
            int id = Integer.parseInt(idStr);
            Product product = findProductById(id);
            if (product == null) {
                out.println("<h3>Không tìm thấy sản phẩm</h3>");
                out.println("<a href='" + contextPath + "/product/index'>Quay lại</a>");
                return;
            }
            out.printf("<h2>Sửa sản phẩm ID = %d</h2>", id);
            out.println("<form method='post' action='" + contextPath + "/product/edit/" + id + "'>");
            out.printf("Tên: <input type='text' name='name' value='%s' required/><br/><br/>", escapeHtml(product.getName()));
            out.printf("Giá: <input type='number' step='0.01' name='price' value='%.2f' required/><br/><br/>", product.getPrice());
            out.println("<input type='submit' value='Cập nhật'/>");
            out.println("</form>");
            out.println("<a href='" + contextPath + "/product/index'>Quay lại danh sách</a>");
        } catch (NumberFormatException e) {
            out.println("<h3>ID không hợp lệ</h3>");
        }
    }

    private void deleteProduct(HttpServletResponse resp, String idStr, String contextPath) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            productList.removeIf(p -> p.getId() == id);
            resp.sendRedirect(contextPath + "/product/index");
        } catch (NumberFormatException e) {
            resp.getWriter().println("<h3>ID không hợp lệ</h3>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String contextPath = req.getContextPath();

        if (pathInfo == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if ("/create".equals(pathInfo)) {
            String name = req.getParameter("name");
            String priceStr = req.getParameter("price");
            if (name != null && priceStr != null) {
                try {
                    double price = Double.parseDouble(priceStr);
                    productList.add(new Product(idCounter.getAndIncrement(), name, price));
                } catch (NumberFormatException ignored) {}
            }
            resp.sendRedirect(contextPath + "/product/index");
        } else if (pathInfo.startsWith("/edit/")) {
            String idStr = pathInfo.substring(6);
            try {
                int id = Integer.parseInt(idStr);
                String name = req.getParameter("name");
                String priceStr = req.getParameter("price");
                if (name != null && priceStr != null) {
                    Product p = findProductById(id);
                    if (p != null) {
                        p.setName(name);
                        p.setPrice(Double.parseDouble(priceStr));
                    }
                }
                resp.sendRedirect(contextPath + "/product/index");
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private Product findProductById(int id) {
        for (Product p : productList) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}