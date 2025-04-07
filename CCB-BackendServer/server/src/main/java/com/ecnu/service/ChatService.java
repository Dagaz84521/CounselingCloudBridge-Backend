package com.ecnu.service;

import com.ecnu.dto.SessionRecordDTO;
import org.springframework.web.socket.WebSocketSession;

public interface ChatService {

    /**
     * 添加会话
     * @return
     */
    void registerSession(Long senderId, WebSocketSession session);

    /**
     * 发送消息给指定会话
     * @return
     */
    void send(SessionRecordDTO dto);
}
