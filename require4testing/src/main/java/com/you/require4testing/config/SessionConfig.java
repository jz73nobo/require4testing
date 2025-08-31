package com.you.require4testing.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

/**
 * Spring Session配置类
 * 启用基于JDBC的HTTP会话管理
 */
@Configuration
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 1800) // 30分钟超时
public class SessionConfig {
    
    // Spring Session会自动配置SessionRepository
    // 不需要额外的bean配置
    
}