package org.example.aiplaner.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
class WebSocketConfiguration {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        // 注册一个配置器，让 Spring 容器来创建端点实例
        return new ServerEndpointExporter();
    }
}
