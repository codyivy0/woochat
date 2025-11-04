package com.woochat.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DatabaseConfig {

    private static final String POSTGRES_DRIVER = "org.postgresql.Driver";

    @Bean
    @Primary
    public DataSource dataSource() {
        // Check Railway environment variables first
        String databaseUrl = System.getenv("DATABASE_URL");
        
        if (databaseUrl == null) {
            databaseUrl = System.getenv("SPRING_DATASOURCE_URL");
        }
        
        if (databaseUrl == null) {
            databaseUrl = System.getProperty("spring.datasource.url");
        }
        
        if (databaseUrl != null) {
            System.out.println("Found database URL: " + sanitizeUrl(databaseUrl));
            return createDataSourceFromUrl(databaseUrl);
        }
        
        System.out.println("No Railway database URL found, using local development configuration");
        return createLocalDataSource();
    }
    
    private DataSource createDataSourceFromUrl(String databaseUrl) {
        if (databaseUrl.startsWith("postgres://")) {
            return createRailwayDataSource(databaseUrl);
        } else if (databaseUrl.startsWith("jdbc:postgresql://")) {
            return createJdbcDataSource(databaseUrl);
        } else {
            throw new IllegalStateException("Unsupported database URL format: " + databaseUrl);
        }
    }
    
    private DataSource createRailwayDataSource(String databaseUrl) {
        try {
            URI uri = new URI(databaseUrl);
            String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + uri.getPort() + uri.getPath();
            String[] userInfo = uri.getUserInfo().split(":");
            String username = userInfo[0];
            String password = userInfo[1];
            
            System.out.println("Using Railway PostgreSQL connection: " + sanitizeUrl(jdbcUrl));
            
            return DataSourceBuilder.create()
                    .url(jdbcUrl)
                    .username(username)
                    .password(password)
                    .driverClassName(POSTGRES_DRIVER)
                    .build();
                    
        } catch (Exception e) {
            System.err.println("Failed to parse Railway DATABASE_URL: " + sanitizeUrl(databaseUrl));
            e.printStackTrace();
            throw new IllegalStateException("Failed to parse DATABASE_URL", e);
        }
    }
    
    private DataSource createJdbcDataSource(String databaseUrl) {
        System.out.println("Using standard JDBC PostgreSQL connection");
        
        String username = System.getenv("PGUSER");
        String password = System.getenv("PGPASSWORD");
        
        if (username == null) username = System.getProperty("spring.datasource.username", "chatuser");
        if (password == null) password = System.getProperty("spring.datasource.password", "chatpass");
        
        return DataSourceBuilder.create()
                .url(databaseUrl)
                .username(username)
                .password(password)
                .driverClassName(POSTGRES_DRIVER)
                .build();
    }
    
    private DataSource createLocalDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/chatdb")
                .username("chatuser")
                .password("chatpass")
                .driverClassName(POSTGRES_DRIVER)
                .build();
    }
    
    private String sanitizeUrl(String url) {
        return url.replaceAll("://.*@", "://***:***@");
    }
}