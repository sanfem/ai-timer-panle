package org.example.aiplaner.controler;

import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.example.aiplaner.DTO.LogMessage;
import org.example.aiplaner.Service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@ServerEndpoint("/api/Ai")
class AiController {
    private AiService aiService;

    @Autowired
    public AiController(AiService aiService){
        this.aiService=aiService;
    }

    private static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        String conversationId = session.getId();
        sessionMap.put(conversationId, session);
        System.out.println("连接建立: " + conversationId);
    }

    /*
    @OnMessage
    public void onMessage(String message, Session session) {
        return aiService.Ai();
    }
    */

}
