package org.spring.training.boot.kafka.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.spring.training.boot.kafka.message.KafkaRecord;
import org.spring.training.boot.kafka.producer.config.KafkaTopicsConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class MessageConsumerService {

    @KafkaListener(topics = KafkaTopicsConfig.DEFAULT_TOPIC_NAME,
            containerFactory = "defaultListenerContainerFactory")
    public void receiveMessage(@Payload KafkaRecord kafkaRecord,
                               @Headers Map<String, Object> headerMap) {
        log.info("Consumer received message: '{}'", kafkaRecord);
        log.info("Headers: {}", headerMap);
    }
}
