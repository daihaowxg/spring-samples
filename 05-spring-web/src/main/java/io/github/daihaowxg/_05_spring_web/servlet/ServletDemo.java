package io.github.daihaowxg._05_spring_web.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Servlet 基本用法演示
 * 
 * <p>
 * 本示例展示了原生 Servlet 的核心功能：
 * <ul>
 * <li>使用 @WebServlet 注解配置 URL 映射</li>
 * <li>处理 GET 和 POST 请求</li>
 * <li>获取请求参数</li>
 * <li>设置响应内容</li>
 * </ul>
 * 
 * <p>
 * <b>访问方式</b>：
 * 
 * <pre>
 * GET  http://localhost:8080/demo/servlet?name=张三
 * POST http://localhost:8080/demo/servlet
 * </pre>
 * 
 * <p>
 * <b>重要说明</b>：
 * <ul>
 * <li>Spring Boot 3.x 使用 Jakarta Servlet 6.0（jakarta.servlet.*）</li>
 * <li>Servlet 是单例的，需要注意线程安全</li>
 * <li>DispatcherServlet 就是继承自 HttpServlet</li>
 * </ul>
 * 
 * @author daihaowxg
 * @since 2026-01-20
 */
@WebServlet(name = "servletDemo", urlPatterns = { "/demo/servlet" }, loadOnStartup = 1 // 启动时加载，值越小优先级越高
)
public class ServletDemo extends HttpServlet {

    /**
     * 处理 GET 请求
     * 
     * @param req  HTTP 请求对象
     * @param resp HTTP 响应对象
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 1. 获取请求参数
        String name = req.getParameter("name");
        if (name == null || name.isEmpty()) {
            name = "游客";
        }

        // 2. 设置响应类型和编码
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // 3. 输出响应内容
        PrintWriter writer = resp.getWriter();
        writer.println("<!DOCTYPE html>");
        writer.println("<html>");
        writer.println("<head><title>Servlet Demo</title></head>");
        writer.println("<body>");
        writer.println("<h1>欢迎，" + escapeHtml(name) + "！</h1>");
        writer.println("<p>这是一个原生 Servlet 示例</p>");
        writer.println("<p><b>请求方式</b>：GET</p>");
        writer.println("<p><b>请求路径</b>：" + req.getRequestURI() + "</p>");
        writer.println("<p><b>查询参数</b>：" + req.getQueryString() + "</p>");
        writer.println("</body>");
        writer.println("</html>");
    }

    /**
     * 处理 POST 请求
     * 
     * @param req  HTTP 请求对象
     * @param resp HTTP 响应对象
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 设置请求体编码
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // 获取表单参数
        String username = req.getParameter("username");
        String email = req.getParameter("email");

        // 设置响应
        resp.setContentType("application/json; charset=UTF-8");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // 返回 JSON 响应
        PrintWriter writer = resp.getWriter();
        writer.println("{");
        writer.println("  \"message\": \"表单提交成功\",");
        writer.println("  \"data\": {");
        writer.println("    \"username\": \"" + escapeJson(username) + "\",");
        writer.println("    \"email\": \"" + escapeJson(email) + "\"");
        writer.println("  }");
        writer.println("}");
    }

    /**
     * HTML 转义，防止 XSS 攻击
     */
    private String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }

    /**
     * JSON 转义
     */
    private String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
