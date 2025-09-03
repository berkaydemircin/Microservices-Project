package com.berkayd.microservices.order;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration
public class TestcontainersConfiguration {
  @Bean
  @ServiceConnection
  MySQLContainer<?> mysql() {
    return new MySQLContainer<>("mysql:8.3.0")
        .withDatabaseName("orders")
        .withUsername("test")
        .withPassword("test")
        .withReuse(false);
  }
}