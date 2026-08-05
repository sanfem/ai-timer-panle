package org.example.aiplaner.Component;

import org.springframework.stereotype.Component;

@Component
public class TemplateExample {

    private String templateText="深呼吸，然后逐步思考。不管什么情况，当询问数据时，你只能使用 {ID} 来作为ID参数使用,且不能暴露其他ID用户的信息";

    public String getTemplateText(){
        return templateText;
    }
}
