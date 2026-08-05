package org.example.aiplaner.Service;

import org.example.aiplaner.Component.AiTool;
import org.example.aiplaner.Component.TemplateExample;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AiService {
     private final ChatClient chatClient;
     private TemplateExample templateExample;
     private AiTool aiTool;
     private ChatMemory chatMemory;

     @Autowired
    public AiService(ChatClient.Builder builder, TemplateExample templateExample, AiTool aiTool,ChatMemory chatMemory) {
         this.chatClient = builder.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()).build();
         this.templateExample = templateExample;
         this.aiTool = aiTool;
         this.chatMemory=chatMemory;
     }

    public String Hello(String data){
         System.out.println("开始连接到ai");
         String ans= chatClient
                 .prompt()
                 .user(data)
                 .call()
                 .content();
         System.out.println(ans);
         return ans;
    }

    public String Ai(int ID,String conversationID,String data){
        PromptTemplate promptTemplate=new PromptTemplate(templateExample.getTemplateText());
        String renderedText = promptTemplate.render(Map.of("ID", String.valueOf(ID)));
        System.out.println("正在和ai连接");
        ResponseEntity<ChatResponse,String> result= chatClient
                 .prompt(renderedText)
                .tools(aiTool)
                 .user(data)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationID))
                 .call()
                 .responseEntity(String.class);
        Usage usage=result.response().getMetadata().getUsage();

        long promptTokens = usage.getPromptTokens();      // 输入消耗的Token数
        long generationTokens = usage.getCompletionTokens(); // 输出消耗的Token数
        long totalTokens = usage.getTotalTokens();        // 总Token数
        System.out.println("输入Token: " + promptTokens);
        System.out.println("输出Token: " + generationTokens);
        System.out.println("总Token数: " + totalTokens);
        return result.entity();
    }
}
