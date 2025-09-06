package com.example.springboot_test.filter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 请求验证过滤器
 * 功能：验证请求频率、登录状态和权限
 */
public class RequestValidationFilter implements Filter {

    // 存储用户请求频率的缓存（Key: 用户ID或IP, Value: 计数器和时间戳）
    private ConcurrentHashMap<String, RequestCounter> requestRateCache = new ConcurrentHashMap<>();

    // 清理过期记录的间隔（毫秒）
    private static final long CLEANUP_INTERVAL = TimeUnit.MINUTES.toMillis(5);
    private long lastCleanupTime = System.currentTimeMillis();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化代码
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 1. 验证请求频率
        if (!validateRequestRate(httpRequest)) {
            httpResponse.setStatus(429); // Too Many Requests
            httpResponse.getWriter().write("请求过于频繁，请稍后再试");
            return;
        }

        // 2. 验证登录状态
        if (!validateLoginStatus(httpRequest)) {
            httpResponse.setStatus(401); // Unauthorized
            httpResponse.getWriter().write("未登录，请先登录");
            return;
        }

        // 3. 验证权限
        if (!validatePermission(httpRequest)) {
            httpResponse.setStatus(403); // Forbidden
            httpResponse.getWriter().write("无权限访问该资源");
            return;
        }

        // 所有验证通过，继续处理请求
        chain.doFilter(request, response);
    }

    /**
     * 验证请求频率（1分钟内不超过10次）
     */
    private boolean validateRequestRate(HttpServletRequest request) {
        String clientId = getClientIdentifier(request);
        long currentTime = System.currentTimeMillis();

        // 定期清理过期记录
        if (currentTime - lastCleanupTime > CLEANUP_INTERVAL) {
            cleanupExpiredRecords(currentTime);
            lastCleanupTime = currentTime;
        }

        // 获取或创建计数器
        RequestCounter counter = requestRateCache.computeIfAbsent(
                clientId, k -> new RequestCounter());

        // 检查时间窗口
        if (currentTime - counter.getStartTime() > TimeUnit.MINUTES.toMillis(1)) {
            // 重置计数器
            counter.reset(currentTime);
        }

        // 增加计数并检查是否超限
        return counter.incrementAndGet() <= 10;
    }

    /**
     * 验证登录状态
     */
    private boolean validateLoginStatus(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        // 检查会话中是否有用户信息
        Object userInfo = session.getAttribute("user");
        return userInfo != null;
    }

    /**
     * 验证权限
     */
    private boolean validatePermission(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }

        // 获取用户权限信息（这里简化处理，实际应从数据库或缓存中获取）
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return false;
        }

        // 获取请求的路径
        String path = request.getRequestURI();

        // 检查用户是否有权限访问该路径
        return user.hasPermission(path);
    }

    private String getClientIdentifier(HttpServletRequest request) {
        // 优先使用已登录用户的ID
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                return "user_" + user.getId();
            }
        }

        return "ip_" + request.getRemoteAddr();
    }


    private void cleanupExpiredRecords(long currentTime) {
        long oneMinuteAgo = currentTime - TimeUnit.MINUTES.toMillis(1);
        requestRateCache.entrySet().removeIf(entry ->
                entry.getValue().getStartTime() < oneMinuteAgo
        );
    }

    @Override
    public void destroy() {
        // 清理资源
        requestRateCache.clear();
    }

    private static class RequestCounter {
        private AtomicInteger count;
        private long startTime;

        public RequestCounter() {
            this.count = new AtomicInteger(0);
            this.startTime = System.currentTimeMillis();
        }

        public int incrementAndGet() {
            return count.incrementAndGet();
        }

        public void reset(long newStartTime) {
            count.set(0);
            startTime = newStartTime;
        }

        public long getStartTime() {
            return startTime;
        }
    }

    private static class User {
        private String id;
        private Set<String> permissions;

        public String getId() {
            return id;
        }

        public boolean hasPermission(String path) {
            return permissions != null && permissions.contains(path);
        }

    }
}