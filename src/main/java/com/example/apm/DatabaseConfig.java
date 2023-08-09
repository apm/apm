package com.example.apm;

import io.micrometer.common.util.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "spring.datasource")
public class DatabaseConfig {

    private String url;
    private String username;
    private String password;
    private String driverClassName;

    // Getter 및 Setter 생략

    @Bean
    @ConditionalOnProperty(name = "SPRING_DATASOURCE_URL")
    public DataSource dataSource() {
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(username) ||
                StringUtils.isEmpty(password) || StringUtils.isEmpty(driverClassName)) {
            throw new IllegalStateException("Database configuration is incomplete.");
        }

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }

    // 다른 설정 메서드 및 빈들 추가

}