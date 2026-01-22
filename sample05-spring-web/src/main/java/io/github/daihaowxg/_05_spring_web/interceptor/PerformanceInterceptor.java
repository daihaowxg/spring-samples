package io.github.daihaowxg._05_spring_web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 性能监控拦截器 - 用于演示多拦截器执行顺序
 * <p>
 * 与 LoggingInterceptor 配合，观察多拦截器的执行顺序：
 * 
 * <pre>
 * preHandle:        LoggingInterceptor → PerformanceInterceptor（按注册顺序）
 * postHandle:       PerformanceInterceptor → LoggingInterceptor（逆序）
 * afterCompletion:  PerformanceInterceptor → LoggingInterceptor（逆序）
 * </pre>
 *
 * @author daihaowxg
 * @since 2026-01-20
 */
@Component
public class PerformanceInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(PerformanceInterceptor.class);

    /** 慢请求阈值（毫秒） */
    private static final long SLOW_REQUEST_THRESHOLD = 100;

    @Override
    @SuppressWarnings("null")
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        request.setAttribute("perfStartTime", System.nanoTime());
        log.debug("  [Performance] 开始计时: {}", request.getRequestURI());
        return true;
    }

    @Override
    @SuppressWarnings("null")
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) throws Exception {

        Long startTime = (Long) request.getAttribute("perfStartTime");
        if (startTime != null) {
            long durationNanos = System.nanoTime() - startTime;
            double durationMs = durationNanos / 1_000_000.0;

            if (durationMs > SLOW_REQUEST_THRESHOLD) {
                log.warn("  [Performance] 慢请求警告! {} {} 耗时 {:.2f} ms",
                        request.getMethod(), request.getRequestURI(), durationMs);
            } else {
                log.debug("  [Performance] 请求耗时: {:.2f} ms", durationMs);
            }
        }
    }
}
