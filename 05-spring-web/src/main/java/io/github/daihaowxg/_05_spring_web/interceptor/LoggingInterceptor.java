package io.github.daihaowxg._05_spring_web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 日志拦截器 - Spring Web MVC 拦截器链源码学习 Demo
 * <p>
 * 本类用于演示和调试拦截器的三个生命周期方法：
 *
 * <h3>执行顺序</h3>
 * 
 * <pre>
 * 请求进入
 *   │
 *   ├─ preHandle()     ← 在 Controller 方法执行前
 *   │     │
 *   │     ▼
 *   │  Controller 方法执行
 *   │     │
 *   ├─ postHandle()    ← 在 Controller 方法执行后，视图渲染前
 *   │     │
 *   │     ▼
 *   │  视图渲染（如果有）
 *   │     │
 *   └─ afterCompletion() ← 请求完成后（无论成功或异常）
 * </pre>
 *
 * <h3>调试入口点</h3>
 * <ol>
 * <li><b>HandlerExecutionChain.applyPreHandle()</b> - 拦截器链 preHandle 执行</li>
 * <li><b>HandlerExecutionChain.applyPostHandle()</b> - 拦截器链 postHandle 执行</li>
 * <li><b>HandlerExecutionChain.triggerAfterCompletion()</b> - 拦截器链
 * afterCompletion 执行</li>
 * </ol>
 *
 * <h3>多拦截器执行顺序</h3>
 * 
 * <pre>
 * preHandle:        拦截器1 → 拦截器2 → 拦截器3（按注册顺序）
 * postHandle:       拦截器3 → 拦截器2 → 拦截器1（逆序）
 * afterCompletion:  拦截器3 → 拦截器2 → 拦截器1（逆序）
 * </pre>
 *
 * @author daihaowxg
 * @since 2026-01-20
 */
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    /**
     * 请求预处理
     * <p>
     * <b>执行时机</b>：Controller 方法执行之前
     * <p>
     * <b>调试重点</b>：
     * <ul>
     * <li>在 DispatcherServlet.doDispatch() 中观察 mappedHandler.applyPreHandle()
     * 调用</li>
     * <li>返回 false 可以中断请求处理，不会执行后续拦截器和 Controller</li>
     * </ul>
     *
     * @param request  当前 HTTP 请求
     * @param response 当前 HTTP 响应
     * @param handler  要执行的处理器（通常是 HandlerMethod）
     * @return true 继续执行，false 中断请求
     */
    @Override
    @SuppressWarnings("null")
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        log.info("═══════════════════════════════════════════════════");
        log.info("【preHandle】请求开始");
        log.info("  ├─ 请求方法: {} {}", request.getMethod(), request.getRequestURI());
        log.info("  ├─ 查询参数: {}", request.getQueryString());
        log.info("  ├─ Handler: {}", handler.getClass().getSimpleName());
        log.info("  └─ 开始时间: {}", startTime);

        // 返回 true 继续执行，返回 false 中断请求
        return true;
    }

    /**
     * 请求后处理
     * <p>
     * <b>执行时机</b>：Controller 方法执行之后，视图渲染之前
     * <p>
     * <b>调试重点</b>：
     * <ul>
     * <li>在 DispatcherServlet.doDispatch() 中观察 mappedHandler.applyPostHandle()
     * 调用</li>
     * <li>可以修改 ModelAndView（如果有）</li>
     * <li>如果 Controller 抛出异常，此方法不会被调用</li>
     * </ul>
     *
     * @param request      当前 HTTP 请求
     * @param response     当前 HTTP 响应
     * @param handler      执行的处理器
     * @param modelAndView 返回的 ModelAndView（REST API 通常为 null）
     */
    @Override
    @SuppressWarnings("null")
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) throws Exception {

        log.info("【postHandle】Controller 执行完成");
        log.info("  ├─ ModelAndView: {}", modelAndView);
        log.info("  └─ Response Status: {}", response.getStatus());
    }

    /**
     * 请求完成后处理
     * <p>
     * <b>执行时机</b>：请求完成后（视图渲染完成或异常处理完成）
     * <p>
     * <b>调试重点</b>：
     * <ul>
     * <li>在 DispatcherServlet.processDispatchResult() 中观察 triggerAfterCompletion()
     * 调用</li>
     * <li>无论请求成功还是异常，此方法都会被调用</li>
     * <li>适合做资源清理、日志记录等收尾工作</li>
     * </ul>
     *
     * @param request  当前 HTTP 请求
     * @param response 当前 HTTP 响应
     * @param handler  执行的处理器
     * @param ex       处理过程中的异常（如果有）
     */
    @Override
    @SuppressWarnings("null")
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) throws Exception {

        Long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long duration = (startTime != null) ? (endTime - startTime) : -1;

        log.info("【afterCompletion】请求完成");
        log.info("  ├─ 耗时: {} ms", duration);
        log.info("  ├─ 异常: {}", ex != null ? ex.getMessage() : "无");
        log.info("  └─ 最终状态: {}", response.getStatus());
        log.info("═══════════════════════════════════════════════════");
    }
}
