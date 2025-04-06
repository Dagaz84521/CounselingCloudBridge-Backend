package com.ecnu.config;

import com.ecnu.handler.ChatWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer  {
    @Autowired // 注入Spring管理的Bean
    private ChatWebSocketHandler chatWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册一个处理器, 端点为 /chat/{sessionId}，允许跨域
        registry.addHandler(chatWebSocketHandler, "/chat/{sessionId}")
                .setAllowedOrigins("*");
    }
}
