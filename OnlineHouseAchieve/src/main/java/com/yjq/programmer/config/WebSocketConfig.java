package com.yjq.programmer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-02-24 23:13
 */

/**
 * WebSocket配置类
 */
@Configuration
public class WebSocketConfig {

    /**
     * 注入ServerEndpointExporter的bean对象，自动注册使用了@ServerEndpoint注解的bean
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

}
