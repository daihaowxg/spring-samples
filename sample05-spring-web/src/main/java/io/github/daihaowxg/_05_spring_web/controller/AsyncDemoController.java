package io.github.daihaowxg._05_spring_web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 演示 Spring MVC 异步请求处理 (Callable, DeferredResult, WebAsyncTask)
 */
@RestController
@RequestMapping("/demo/api/async")
public class AsyncDemoController {

    private static final Logger log = LoggerFactory.getLogger(AsyncDemoController.class);

    /**
     * 1. 使用 Callable
     * Spring MVC 会将任务提交到内部配置的 TaskExecutor 中执行。
     * 当任务执行完后，会再次分发请求完成响应。
     */
    @GetMapping("/callable")
    public Callable<String> handleCallable() {
        log.info("进入 /callable，主线程：{}", Thread.currentThread().getName());

        return () -> {
            log.info("异步子线程开始处理：{}", Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(2); // 模拟长耗时操作
            log.info("异步处理完成");
            return "Callable 响应结果 (来自线程: " + Thread.currentThread().getName() + ")";
        };
    }

    /**
     * 2. 使用 DeferredResult
     * 最灵活的方案。可以在任何线程中、任何时间点（甚至是由外部事件触发）手动设置结果。
     * 常用于：长轮询、WebSocket 消息通知。
     */
    @GetMapping("/deferred-result")
    public DeferredResult<String> handleDeferredResult() {
        log.info("进入 /deferred-result，主线程：{}", Thread.currentThread().getName());

        // 设置超时时间为 5 秒
        DeferredResult<String> output = new DeferredResult<>(5000L, "处理超时了！");

        // 模拟外部异步任务（如从消息队列或另一个线程获取结果）
        CompletableFuture.runAsync(() -> {
            try {
                log.info("外部异步线程正在处理：{}", Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(3);
                output.setResult("DeferredResult 响应结果 (来自外部线程)");
            } catch (InterruptedException e) {
                output.setErrorResult(e);
            }
        });

        output.onCompletion(() -> log.info("DeferredResult 处理完成（不管是成功还是超时）"));

        return output;
    }

    /**
     * 3. 使用 WebAsyncTask
     * 是对 Callable 的加强，允许显式指定超时时间、异常处理器以及特定的执行器。
     */
    @GetMapping("/web-async-task")
    public WebAsyncTask<String> handleWebAsyncTask() {
        log.info("进入 /web-async-task，主线程：{}", Thread.currentThread().getName());

        Callable<String> callable = () -> {
            log.info("WebAsyncTask 任务线程控制：{}", Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(2);
            return "WebAsyncTask 响应成功";
        };

        // 设置 3 秒超时
        WebAsyncTask<String> asyncTask = new WebAsyncTask<>(3000L, callable);

        asyncTask.onTimeout(() -> {
            log.warn("WebAsyncTask 任务超时了");
            return "超时 Fallback 结果";
        });

        asyncTask.onCompletion(() -> log.info("WebAsyncTask 流程结束"));

        return asyncTask;
    }
}
