/**
 * CompletableFuture 学习模块
 *
 * <h2>学习路径</h2>
 * <ol>
 *   <li>基础创建：{@code supplyAsync}, {@code runAsync}</li>
 *   <li>结果处理：{@code thenApply}, {@code thenAccept}, {@code thenRun}</li>
 *   <li>组合操作：{@code thenCompose}, {@code thenCombine}</li>
 *   <li>异常处理：{@code exceptionally}, {@code handle}, {@code whenComplete}</li>
 *   <li>多任务协调：{@code allOf}, {@code anyOf}</li>
 * </ol>
 *
 * <h2>核心概念</h2>
 * <ul>
 *   <li>CompletableFuture 是 JDK 8 引入的异步编程工具</li>
 *   <li>支持函数式链式调用</li>
 *   <li>默认使用 ForkJoinPool.commonPool() 执行异步任务</li>
 * </ul>
 */
@org.jspecify.annotations.NullMarked
package io.github.daihaowxg.completablefuture;
