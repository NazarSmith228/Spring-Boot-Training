package org.spring.training.boot.kafka.producer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.spring.training.boot.kafka.message.KafkaRecord;
import org.spring.training.boot.kafka.producer.config.KafkaTopicsConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProducerService {

    private static long counter = 0;

    private final KafkaTemplate<String, KafkaRecord> producerTemplate;

    @Scheduled(initialDelay = 5, fixedDelay = 10, timeUnit = TimeUnit.SECONDS)
    public void sendToDefaultTopic() {
        String recordName = "Kafka record #%d".formatted(++counter);
        KafkaRecord kafkaRecord = new KafkaRecord(recordName, LocalDateTime.now());

        ListenableFuture<SendResult<String, KafkaRecord>> futureResult =
                producerTemplate.send(KafkaTopicsConfig.DEFAULT_TOPIC_NAME, kafkaRecord);

        futureResult.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onFailure(@NonNull Throwable ex) {
                log.error("Unable to send record `{}` - an error occurred: {}", recordName, ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, KafkaRecord> result) {
                ProducerRecord<String, KafkaRecord> producerRecord = result.getProducerRecord();
                RecordMetadata recordMetadata = result.getRecordMetadata();
                String topic = recordMetadata.topic();
                String key = producerRecord.key();
                int partition = recordMetadata.partition();
                long offset = recordMetadata.offset();

                log.info("Successfully sent record '{}' to topic '{}'. Key: '{}', partition: '{}', offset: '{}'",
                        recordName, topic, key, partition, offset);
            }
        });
    }

}
