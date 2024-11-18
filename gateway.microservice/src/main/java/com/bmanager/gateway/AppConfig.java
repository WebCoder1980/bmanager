package com.bmanager.gateway;

import com.bmanager.gateway.client.AuthClient;
import com.bmanager.gateway.client.UserClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AppConfig {
    @Bean
    public Logger getLogger() {
        return LogManager.getLogger(Application.class);
    }


    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AuthClient getAuthClient(RestTemplate restTemplate) {
        return new AuthClient(restTemplate);
    }

    @Bean
    public UserClient getUserClient(RestTemplate restTemplate) {
        return new UserClient(restTemplate);
    }
}
