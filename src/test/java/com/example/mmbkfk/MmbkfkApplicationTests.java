package com.example.mmbkfk;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
class MmbkfkApplicationTests {

    private final static String TOPIC_NAME = "zjwqq"; //topic的名称

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Test
    void contextLoads() {

        kafkaTemplate.send(TOPIC_NAME,"123","07123");
        String defaultTopic = kafkaTemplate.getDefaultTopic();
        System.out.println(defaultTopic);


    }

}
