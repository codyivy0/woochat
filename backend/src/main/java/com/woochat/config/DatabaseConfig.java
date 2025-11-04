package com.woochat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        // Check Railway environment variables (Railway uses DATABASE_URL or SPRING_DATASOURCE_URL)
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null) {
            databaseUrl = System.getenv("SPRING_DATASOURCE_URL");
        }
        
        if (databaseUrl != null && databaseUrl.startsWith("postgres://")) {
            // Convert Railway's postgres:// URL to JDBC format
            try {
                URI uri = new URI(databaseUrl);
                String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + uri.getPort() + uri.getPath();
                String username = uri.getUserInfo().split(":")[0];
                String password = uri.getUserInfo().split(":")[1];
                
                System.out.println("Using Railway PostgreSQL connection: " + jdbcUrl);
                
                return DataSourceBuilder.create()
                        .url(jdbcUrl)
                        .username(username)
                        .password(password)
                        .driverClassName("org.postgresql.Driver")
                        .build();
                        
            } catch (Exception e) {
                throw new IllegalStateException("Failed to parse DATABASE_URL: " + databaseUrl, e);
            }
        }
        
        System.out.println("Using local development PostgreSQL connection");
        
        // For local development, create DataSource with properties from application.properties
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/chatdb")
                .username("chatuser")
                .password("chatpass")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}