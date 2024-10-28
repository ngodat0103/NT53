package com.example.yamp.usersvc;


import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

@SpringBootTest
@ActiveProfiles("integration-test")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class IntegrationTest {
    private static final PostgreSQLContainer<?> postgreSQLContainer =new PostgreSQLContainer<>("postgres:16.3-bullseye");
    private static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.0"));
    @BeforeAll
    static void startContainer() throws IOException, InterruptedException {
        postgreSQLContainer.start();
        postgreSQLContainer.execInContainer("create","extension","uuid-ossp");
        kafkaContainer.start();
    }
    @AfterAll
    static void stopContainer() {
        postgreSQLContainer.stop();
        kafkaContainer.stop();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }
    @Test
    @Order(1)
    void contextLoads() {
    }
}
