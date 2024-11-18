package com.bmanager.gateway;

import com.bmanager.gateway.util.MicroservicesPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayConfig {
    private final CustomFilter customFilter;

    @Autowired
    public GatewayConfig(CustomFilter customFilter) {
        this.customFilter = customFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-microservice", r -> r.path("/auth/**")
                        .uri(MicroservicesPath.AUTH.getPathForGateway()))
                .route("users-microservice", r -> r.path("/users/**")
                        .filters(f -> f.filter(customFilter.apply(new CustomFilter.Config())))
                        .uri(MicroservicesPath.USERS.getPathForGateway()))
                .route("dirs-microservice", r -> r.path("/dirs/**")
                        .filters(f -> f.filter(customFilter.apply(new CustomFilter.Config())))
                        .uri(MicroservicesPath.DIRS.getPathForGateway()))
                .route("notebooks-microservice", r -> r.path("/notebooks/**")
                        .filters(f -> f.filter(customFilter.apply(new CustomFilter.Config())))
                        .uri(MicroservicesPath.NOTEBOOKS.getPathForGateway()))

                .route("echo", r -> r.path("/get/**")
                        .filters(f -> f.filter(customFilter.apply(new CustomFilter.Config())))
                        .uri("https://postman-echo.com/get"))

                .build();
    }
}