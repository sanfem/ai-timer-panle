package org.example.aiplaner.controler;

import org.example.aiplaner.Service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/AI")
class AiController {
    private AiService aiService;

    @Autowired
    public AiController(AiService aiService) {
        this.aiService = aiService;
    }



    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> send(@RequestAttribute long userId,@RequestBody Map<String,String> request){
        String message=request.get("message");
        String res=aiService.Ai(userId,message);
        System.out.println(res);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("content",res));
    }


}
