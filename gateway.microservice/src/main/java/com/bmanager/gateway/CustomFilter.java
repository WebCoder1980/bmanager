package com.bmanager.gateway;

import com.bmanager.gateway.client.AuthClient;
import com.bmanager.gateway.client.UserClient;
import com.bmanager.gateway.dto.JwtDataResponse;
import com.bmanager.gateway.dto.UserDataResponse;
import com.bmanager.gateway.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    private final Logger logger;
    private final ObjectMapper objectMapper;
    private final AuthClient authClient;
    private final UserClient userClient;

    @Autowired
    public CustomFilter(Logger logger, ObjectMapper objectMapper, AuthClient authClient, UserClient userClient) {
        this.logger = logger;
        this.objectMapper = objectMapper;
        this.authClient = authClient;
        this.userClient = userClient;
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
            HttpHeaders responseHeaders = exchange.getResponse().getHeaders();
            HttpHeaders requestHeaders = exchange.getRequest().getHeaders();

            if (requestHeaders.get("X-user") != null) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION).substring(7);

            JwtDataResponse userDataResponse = authClient.getByToken(token);

            String userResponseString = null;
            try {
                userResponseString = objectMapper.writeValueAsString(userClient.getById(userDataResponse.getUserInfo().getId()));
            } catch(Exception e) {
                logger.error(e);
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            builder.header("X-user", userResponseString);

            return chain.filter(exchange.mutate().request(builder.build()).build());
        };
    }
}
