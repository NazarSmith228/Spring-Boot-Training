package org.spring.training.boot.kafka.message;

import java.time.LocalDateTime;

public record KafkaRecord(String name, LocalDateTime creationDate) {
}
