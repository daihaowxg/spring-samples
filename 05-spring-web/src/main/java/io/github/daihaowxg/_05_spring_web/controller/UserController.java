package io.github.daihaowxg._05_spring_web.controller;

import io.github.daihaowxg._05_spring_web.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用户控制器 - Spring Web MVC 源码学习 Demo
 * <p>
 * 本类用于演示和调试以下 Spring Web MVC 核心流程：
 *
 * <h3>调试入口点</h3>
 * <ol>
 * <li><b>DispatcherServlet.doDispatch()</b> - 请求分发主流程</li>
 * <li><b>RequestMappingHandlerMapping.getHandlerInternal()</b> - 路由匹配</li>
 * <li><b>RequestMappingHandlerAdapter.handleInternal()</b> - 执行控制器方法</li>
 * <li><b>PathVariableMethodArgumentResolver</b> - @PathVariable 参数解析</li>
 * <li><b>RequestResponseBodyMethodProcessor</b> - @RequestBody/@ResponseBody
 * 处理</li>
 * </ol>
 *
 * <h3>推荐调试步骤</h3>
 * 
 * <pre>
 * 1. 在 DispatcherServlet.doDispatch() 方法打断点
 * 2. 发送 HTTP 请求到 /api/users/1
 * 3. 使用 Step Into 逐步跟踪请求处理流程
 * </pre>
 *
 * @author daihaowxg
 * @since 2026-01-19
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /** 模拟数据存储 */
    private final Map<Long, User> userStore = new ConcurrentHashMap<>();

    /** ID 生成器 */
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserController() {
        // 初始化测试数据
        userStore.put(1L, new User(1L, "张三", "zhangsan@example.com"));
        userStore.put(2L, new User(2L, "李四", "lisi@example.com"));
    }

    /**
     * 获取所有用户
     * <p>
     * 调试点：观察 @ResponseBody 如何将 List 序列化为 JSON
     * 
     * @return 用户列表
     */
    @GetMapping
    public List<User> getAllUsers() {
        return List.copyOf(userStore.values());
    }

    /**
     * 根据 ID 获取用户
     * <p>
     * <b>调试重点</b>：
     * <ul>
     * <li>PathVariableMethodArgumentResolver.resolveArgument() -
     * 解析 @PathVariable</li>
     * <li>StringToNumberConverterFactory - String 转 Long 类型转换</li>
     * </ul>
     *
     * @param id 用户 ID（路径变量）
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        User user = userStore.get(id);
        if (user == null) {
            // Demo: 后续可添加异常处理演示
            return new User(id, "未找到用户");
        }
        return user;
    }

    /**
     * 创建用户
     * <p>
     * <b>调试重点</b>：
     * <ul>
     * <li>RequestResponseBodyMethodProcessor.resolveArgument() -
     * 解析 @RequestBody</li>
     * <li>MappingJackson2HttpMessageConverter.read() - JSON 反序列化</li>
     * <li>RequestResponseBodyMethodProcessor.handleReturnValue() - 返回值处理</li>
     * <li>MappingJackson2HttpMessageConverter.write() - JSON 序列化</li>
     * </ul>
     *
     * @param user 用户信息（请求体 JSON）
     * @return 创建后的用户（包含生成的 ID）
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        Long newId = idGenerator.getAndIncrement();
        user.setId(newId);
        userStore.put(newId, user);
        return user;
    }

    /**
     * 更新用户
     * <p>
     * 演示同时使用 @PathVariable 和 @RequestBody
     *
     * @param id   用户 ID
     * @param user 更新的用户信息
     * @return 更新后的用户
     */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userStore.put(id, user);
        return user;
    }

    /**
     * 删除用户
     *
     * @param id 用户 ID
     * @return 删除结果消息
     */
    @DeleteMapping("/{id}")
    public Map<String, String> deleteUser(@PathVariable Long id) {
        User removed = userStore.remove(id);
        if (removed != null) {
            return Map.of("message", "用户 " + id + " 已删除");
        }
        return Map.of("message", "用户 " + id + " 不存在");
    }

    /**
     * 搜索用户（演示 @RequestParam）
     * <p>
     * <b>调试重点</b>：
     * <ul>
     * <li>RequestParamMethodArgumentResolver.resolveArgument() -
     * 解析 @RequestParam</li>
     * </ul>
     *
     * @param name 用户名（查询参数）
     * @return 匹配的用户列表
     */
    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam(required = false) String name) {
        if (name == null || name.isBlank()) {
            return List.copyOf(userStore.values());
        }
        return userStore.values().stream()
                .filter(user -> user.getName().contains(name))
                .toList();
    }
}
