package io.github.daihaowxg._05_spring_web.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Servlet 生命周期演示
 * 
 * <p>
 * 本示例演示 Servlet 的三个生命周期阶段：
 * <ol>
 * <li><b>初始化阶段</b>：容器调用 {@link #init(ServletConfig)} 方法（只执行一次）</li>
 * <li><b>服务阶段</b>：容器调用
 * {@link #service(HttpServletRequest, HttpServletResponse)} 方法（每次请求都执行）</li>
 * <li><b>销毁阶段</b>：容器调用 {@link #destroy()} 方法（只执行一次）</li>
 * </ol>
 * 
 * <p>
 * <b>生命周期关键点</b>：
 * <ul>
 * <li>Servlet 容器为每个 Servlet 创建唯一实例（单例模式）</li>
 * <li>init() 在第一次请求时执行，或通过 loadOnStartup 配置在启动时执行</li>
 * <li>service() 方法会根据请求类型调用 doGet()、doPost() 等方法</li>
 * <li>destroy() 在应用关闭或 Servlet 被卸载时执行</li>
 * <li>由于是单例，所以实例变量不是线程安全的</li>
 * </ul>
 * 
 * <p>
 * <b>访问方式</b>：
 * 
 * <pre>
 * GET http://localhost:8080/demo/lifecycle
 * </pre>
 * 
 * <p>
 * <b>观察方式</b>：
 * <ul>
 * <li>启动应用，观察控制台输出的 init() 日志</li>
 * <li>访问 URL，观察每次请求的 service() 日志</li>
 * <li>停止应用，观察 destroy() 日志</li>
 * </ul>
 * 
 * @author daihaowxg
 * @since 2026-01-20
 */
@WebServlet(name = "servletLifecycleDemo", urlPatterns = { "/demo/lifecycle" }, loadOnStartup = 1 // 应用启动时立即初始化
)
public class ServletLifecycleDemo extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ServletLifecycleDemo.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 初始化时间（实例变量，演示单例特性）
     */
    private String initTime;

    /**
     * 请求计数器（实例变量，注意：不是线程安全的！）
     * 这里仅用于演示，生产环境应使用 AtomicInteger
     */
    private int requestCount = 0;

    /**
     * 【生命周期阶段 1】初始化
     * 
     * <p>
     * 容器在以下情况调用 init() 方法：
     * <ul>
     * <li>首次请求到达时（默认行为）</li>
     * <li>应用启动时（配置了 loadOnStartup >= 0）</li>
     * </ul>
     * 
     * <p>
     * <b>调用时机</b>：只执行一次
     * <p>
     * <b>主要用途</b>：
     * <ul>
     * <li>加载配置参数</li>
     * <li>初始化资源（数据库连接池、缓存等）</li>
     * <li>设置实例变量</li>
     * </ul>
     *
     * @param config Servlet 配置对象
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        initTime = LocalDateTime.now().format(FORMATTER);

        log.info("========================================");
        log.info("【生命周期】Servlet 初始化");
        log.info("Servlet 名称: {}", config.getServletName());
        log.info("初始化时间: {}", initTime);
        log.info("loadOnStartup 配置: 1（应用启动时立即加载）");
        log.info("========================================");
    }

    /**
     * 【生命周期阶段 2】服务
     * 
     * <p>
     * 容器在每次请求到达时调用 service() 方法。
     * <p>
     * 该方法会根据 HTTP 请求方式（GET、POST 等）调用对应的 doXxx() 方法。
     * 
     * <p>
     * <b>调用时机</b>：每次 HTTP 请求都会执行
     * <p>
     * <b>执行流程</b>：
     * 
     * <pre>
     * 客户端请求 → Servlet 容器 → service() → doGet()/doPost()/... → 响应客户端
     * </pre>
     *
     * @param req  HTTP 请求对象
     * @param resp HTTP 响应对象
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 请求计数（注意：这里不是线程安全的，仅用于演示）
        requestCount++;

        String currentTime = LocalDateTime.now().format(FORMATTER);

        log.info("----------------------------------------");
        log.info("【生命周期】处理请求");
        log.info("请求方式: {}", req.getMethod());
        log.info("请求路径: {}", req.getRequestURI());
        log.info("当前时间: {}", currentTime);
        log.info("累计请求次数: {}", requestCount);
        log.info("----------------------------------------");

        // 调用父类的 service() 方法，它会根据请求方式调用 doGet()、doPost() 等
        super.service(req, resp);
    }

    /**
     * 处理 GET 请求
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        PrintWriter writer = resp.getWriter();
        writer.println("<!DOCTYPE html>");
        writer.println("<html>");
        writer.println("<head><title>Servlet 生命周期演示</title></head>");
        writer.println("<body style='font-family: Arial, sans-serif; padding: 20px;'>");
        writer.println("<h1>🔄 Servlet 生命周期演示</h1>");

        writer.println("<h2>生命周期状态</h2>");
        writer.println("<table border='1' cellpadding='10' style='border-collapse: collapse;'>");
        writer.println("<tr><th>阶段</th><th>方法</th><th>执行次数</th><th>状态</th></tr>");
        writer.println("<tr><td>1. 初始化</td><td>init()</td><td>1 次</td><td>✅ 已执行（" + initTime + "）</td></tr>");
        writer.println(
                "<tr><td>2. 服务</td><td>service()</td><td>每次请求</td><td>✅ 执行中（已处理 " + requestCount + " 次请求）</td></tr>");
        writer.println("<tr><td>3. 销毁</td><td>destroy()</td><td>1 次</td><td>⏳ 未执行（应用关闭时调用）</td></tr>");
        writer.println("</table>");

        writer.println("<h2>关键特性</h2>");
        writer.println("<ul>");
        writer.println("<li><b>单例模式</b>：整个应用生命周期内只有一个 Servlet 实例</li>");
        writer.println("<li><b>线程不安全</b>：实例变量会被多个线程共享，需要注意并发问题</li>");
        writer.println("<li><b>初始化时机</b>：本示例配置了 loadOnStartup=1，在应用启动时初始化</li>");
        writer.println("</ul>");

        writer.println("<h2>观察建议</h2>");
        writer.println("<ol>");
        writer.println("<li>查看控制台日志，观察 init() 方法的执行时机</li>");
        writer.println("<li>刷新页面多次，观察请求计数器的变化</li>");
        writer.println("<li>停止应用，观察 destroy() 方法的执行</li>");
        writer.println("</ol>");

        writer.println("<p><a href='" + req.getRequestURI() + "'>🔄 刷新页面</a></p>");
        writer.println("</body>");
        writer.println("</html>");
    }

    /**
     * 【生命周期阶段 3】销毁
     * 
     * <p>
     * 容器在以下情况调用 destroy() 方法：
     * <ul>
     * <li>Web 应用停止</li>
     * <li>Web 应用重新加载</li>
     * <li>Servlet 容器关闭</li>
     * </ul>
     * 
     * <p>
     * <b>调用时机</b>：只执行一次
     * <p>
     * <b>主要用途</b>：
     * <ul>
     * <li>释放资源（关闭数据库连接、文件流等）</li>
     * <li>保存状态信息</li>
     * <li>清理缓存</li>
     * </ul>
     */
    @Override
    public void destroy() {
        String destroyTime = LocalDateTime.now().format(FORMATTER);

        log.info("========================================");
        log.info("【生命周期】Servlet 销毁");
        log.info("销毁时间: {}", destroyTime);
        log.info("初始化时间: {}", initTime);
        log.info("总请求次数: {}", requestCount);
        log.info("生命周期结束，释放资源...");
        log.info("========================================");

        // 在这里释放资源
        // 例如：关闭数据库连接、清理缓存等
    }
}
