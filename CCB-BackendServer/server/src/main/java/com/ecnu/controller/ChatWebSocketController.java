package com.ecnu.controller;

import com.ecnu.dto.SessionRecordDTO;
import com.ecnu.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ChatWebSocketController {
    @Autowired
    private ChatService chatService;

    @MessageMapping("/session/send")
    public void handleMessage(@RequestBody SessionRecordDTO sessionRecordDTO) {
//        chatService.sendMessage(sessionRecordDTO);
    }
}

