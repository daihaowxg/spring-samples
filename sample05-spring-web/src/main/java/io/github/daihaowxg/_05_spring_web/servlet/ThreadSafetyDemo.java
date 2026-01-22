package io.github.daihaowxg._05_spring_web.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Servlet 线程安全演示
 * 
 * <p>
 * 本示例演示 Servlet 的单例特性和线程安全问题：
 * <ul>
 * <li><b>单例模式</b>：整个应用只有一个 Servlet 实例</li>
 * <li><b>多线程并发</b>：多个请求线程同时调用同一个实例</li>
 * <li><b>线程安全问题</b>：实例变量会被多个线程共享，产生竞态条件</li>
 * </ul>
 * 
 * <p>
 * <b>访问方式</b>：
 * 
 * <pre>
 * GET http://localhost:8080/demo/thread-safety
 * </pre>
 * 
 * <p>
 * <b>测试方法</b>：
 * <ol>
 * <li>启动应用</li>
 * <li>在浏览器中打开多个标签页，同时访问该 URL</li>
 * <li>观察响应中的 Servlet 实例 hashCode（都相同）</li>
 * <li>观察实例计数器的值（会出现错误，不是预期的值）</li>
 * <li>观察局部计数器的值（始终正确，每个线程都是 5）</li>
 * </ol>
 * 
 * <p>
 * <b>关键结论</b>：
 * <ul>
 * <li>✅ 所有请求使用同一个 Servlet 实例（单例）</li>
 * <li>✅ 每个请求使用不同的线程</li>
 * <li>❌ 实例变量不是线程安全的</li>
 * <li>✅ 局部变量是线程安全的</li>
 * </ul>
 * 
 * @author daihaowxg
 * @since 2026-01-20
 */
@WebServlet(name = "threadSafetyDemo", urlPatterns = { "/demo/thread-safety" }, loadOnStartup = 1)
public class ThreadSafetyDemo extends HttpServlet {

    /**
     * 实例变量（被所有线程共享）
     * 
     * <p>
     * <b>问题</b>：多个线程会同时修改这个变量，导致竞态条件
     * <p>
     * <b>现象</b>：最终值可能不是预期的累加结果
     */
    private int instanceCounter = 0;

    /**
     * 处理 GET 请求
     * 
     * <p>
     * 模拟耗时操作，增加线程并发冲突的概率
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // 局部变量（每个线程独立，存储在线程栈中）
        int localCounter = 0;

        // 模拟耗时操作，增加并发冲突概率
        for (int i = 0; i < 5; i++) {
            // 实例变量：多线程不安全
            instanceCounter++;

            // 局部变量：线程安全
            localCounter++;

            // 休眠 100ms，增加线程交错的概率
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }

        // 设置响应
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        PrintWriter writer = resp.getWriter();
        writer.println("<!DOCTYPE html>");
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>Servlet 线程安全演示</title>");
        writer.println("<style>");
        writer.println("body { font-family: Arial, sans-serif; padding: 20px; }");
        writer.println("table { border-collapse: collapse; width: 100%; margin: 20px 0; }");
        writer.println("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
        writer.println("th { background-color: #4CAF50; color: white; }");
        writer.println(".error { color: red; font-weight: bold; }");
        writer.println(".success { color: green; font-weight: bold; }");
        writer.println(
                ".warning { background-color: #fff3cd; padding: 15px; margin: 15px 0; border-left: 4px solid #ffc107; }");
        writer.println("</style>");
        writer.println("</head>");
        writer.println("<body>");

        writer.println("<h1>🔒 Servlet 线程安全演示</h1>");

        // 显示测试结果
        writer.println("<h2>测试结果</h2>");
        writer.println("<table>");
        writer.println("<tr><th>项目</th><th>值</th><th>说明</th></tr>");

        writer.println("<tr>");
        writer.println("<td>当前线程</td>");
        writer.println("<td>" + Thread.currentThread().getName() + "</td>");
        writer.println("<td>每个请求使用不同的线程</td>");
        writer.println("</tr>");

        writer.println("<tr>");
        writer.println("<td>Servlet 实例</td>");
        writer.println("<td>" + this.hashCode() + "</td>");
        writer.println("<td class='success'>所有请求的 hashCode 相同 → 证明是单例</td>");
        writer.println("</tr>");

        writer.println("<tr>");
        writer.println("<td class='error'>实例计数器</td>");
        writer.println("<td class='error'>" + instanceCounter + "</td>");
        writer.println("<td class='error'>❌ 不是预期的累加值（应该是请求数×5）</td>");
        writer.println("</tr>");

        writer.println("<tr>");
        writer.println("<td class='success'>局部计数器</td>");
        writer.println("<td class='success'>" + localCounter + "</td>");
        writer.println("<td class='success'>✅ 始终是 5（线程安全）</td>");
        writer.println("</tr>");

        writer.println("</table>");

        // 显示说明
        writer.println("<div class='warning'>");
        writer.println("<h3>⚠️ 线程安全问题说明</h3>");
        writer.println("<ul>");
        writer.println("<li><b>实例变量（instanceCounter）</b>：被所有线程共享，存在竞态条件</li>");
        writer.println("<li><b>局部变量（localCounter）</b>：每个线程独立，线程安全</li>");
        writer.println("<li><b>原因</b>：instanceCounter++ 不是原子操作，分为 读取-加1-写回 三步</li>");
        writer.println("<li><b>现象</b>：多个线程可能同时读取相同的值，导致累加丢失</li>");
        writer.println("</ul>");
        writer.println("</div>");

        // 使用建议
        writer.println("<h2>解决方案</h2>");
        writer.println("<ol>");
        writer.println("<li><b>使用局部变量</b>（推荐）：将数据存储在方法内部，每个线程独立</li>");
        writer.println("<li><b>使用 AtomicInteger</b>：实例变量使用线程安全类</li>");
        writer.println("<li><b>使用 synchronized</b>（不推荐）：会降低性能</li>");
        writer.println("</ol>");

        // 代码示例
        writer.println("<h2>正确做法示例</h2>");
        writer.println("<pre>");
        writer.println("// ✅ 方案 1：使用局部变量");
        writer.println("@Override");
        writer.println("protected void doGet(...) {");
        writer.println("    int count = 0;  // 局部变量，线程安全");
        writer.println("    count++;");
        writer.println("}");
        writer.println("");
        writer.println("// ✅ 方案 2：使用 AtomicInteger");
        writer.println("private final AtomicInteger counter = new AtomicInteger(0);");
        writer.println("");
        writer.println("@Override");
        writer.println("protected void doGet(...) {");
        writer.println("    counter.incrementAndGet();  // 原子操作");
        writer.println("}");
        writer.println("</pre>");

        // 测试建议
        writer.println("<h2>测试建议</h2>");
        writer.println("<p>在多个浏览器标签页中同时刷新此页面，观察：</p>");
        writer.println("<ul>");
        writer.println("<li>Servlet 实例 hashCode 始终相同（证明单例）</li>");
        writer.println("<li>线程名称不同（证明多线程）</li>");
        writer.println("<li>实例计数器的值不符合预期（证明线程不安全）</li>");
        writer.println("<li>局部计数器始终是 5（证明线程安全）</li>");
        writer.println("</ul>");

        writer.println("<p><a href='" + req.getRequestURI() + "'>🔄 刷新页面</a></p>");

        writer.println("</body>");
        writer.println("</html>");
    }
}
