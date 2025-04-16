package com.ecnu.service;

import com.ecnu.dto.MessageDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.socket.WebSocketSession;

public interface ChatService {

    /**
     * 添加会话
     * @return
     */
    void registerSession(Long senderId, WebSocketSession session);

    /**
     * 发送消息给指定会话(咨询师和客户之间)
     * @return
     */
    void sendToSession(MessageDTO dto) throws JsonProcessingException;

    /**
     * 发送消息给指定Request(咨询师和督导之间)
     * @return
     */
    void sendToRequest(MessageDTO dto);
}
