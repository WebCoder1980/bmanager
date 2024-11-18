package com.bmanager.dirs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class AppConfig {
    private SessionFactory sessionFactory = buildSessionFactory();

    protected SessionFactory buildSessionFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new ExceptionInInitializerError("Initial SessionFactory failed" + e);
        }

        return sessionFactory;
    }

    @Bean
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Bean
    public Logger getLogger() {
        return LogManager.getLogger(Application.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}