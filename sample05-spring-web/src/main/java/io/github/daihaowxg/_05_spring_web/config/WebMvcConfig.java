package io.github.daihaowxg._05_spring_web.config;

import io.github.daihaowxg._05_spring_web.interceptor.LoggingInterceptor;
import io.github.daihaowxg._05_spring_web.interceptor.PerformanceInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 配置类 - 拦截器注册 Demo
 * <p>
 * 本类演示如何通过 WebMvcConfigurer 注册拦截器。
 *
 * <h3>调试入口点</h3>
 * <ol>
 * <li><b>WebMvcConfigurationSupport.getInterceptors()</b> - 获取所有拦截器</li>
 * <li><b>AbstractHandlerMapping.getHandler()</b> - 构建
 * HandlerExecutionChain（包含拦截器链）</li>
 * <li><b>MappedInterceptor.matches()</b> - 判断拦截器是否匹配当前请求</li>
 * </ol>
 *
 * <h3>拦截器执行顺序</h3>
 * <p>
 * 注册顺序决定了 preHandle 的执行顺序：
 * 
 * <pre>
 * registry.addInterceptor(loggingInterceptor)     // 第一个注册 → preHandle 第一个执行
 * registry.addInterceptor(performanceInterceptor) // 第二个注册 → preHandle 第二个执行
 * </pre>
 * <p>
 * postHandle 和 afterCompletion 的执行顺序相反（逆序）。
 *
 * @author daihaowxg
 * @since 2026-01-20
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;
    private final PerformanceInterceptor performanceInterceptor;

    public WebMvcConfig(LoggingInterceptor loggingInterceptor,
            PerformanceInterceptor performanceInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
        this.performanceInterceptor = performanceInterceptor;
    }

    /**
     * 注册拦截器
     * <p>
     * <b>调试重点</b>：
     * <ul>
     * <li>addPathPatterns() - 指定拦截路径</li>
     * <li>excludePathPatterns() - 排除路径</li>
     * <li>order() - 指定执行顺序（数值越小越先执行）</li>
     * </ul>
     *
     * @param registry 拦截器注册器
     */
    @Override
    @SuppressWarnings("null")
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册日志拦截器 - 拦截 /api/** 路径
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/api/**") // 拦截 /api/ 下的所有请求
                .excludePathPatterns("/api/health") // 排除健康检查接口
                .order(1); // 执行顺序：1（先执行）

        // 注册性能监控拦截器
        registry.addInterceptor(performanceInterceptor)
                .addPathPatterns("/api/**")
                .order(2); // 执行顺序：2（后执行）
    }
}
