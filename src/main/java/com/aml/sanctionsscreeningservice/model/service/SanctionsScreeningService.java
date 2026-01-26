package com.aml.sanctionsscreeningservice.model.service;

import com.aml.sanctionsscreeningservice.model.model.ScreeningResult;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SanctionsScreeningService {
    private final KafkaTemplate<String, ScreeningResult> kafkaTemplate;
    private final String inputTopic;
    private final String outputTopic;

    public SanctionsScreeningService(
            KafkaTemplate<String, ScreeningResult> kafkaTemplate,
            @Value("${kafka.topics.transactions.raw}") String inputTopic,
            @Value("${kafka.topics.transactions.sanctions}") String outputTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;
    }

    @KafkaListener(topics = "${kafka.topics.transactions.raw}")
    public void checkSanctions(ConsumerRecord<String, Transaction> record) {
        ScreeningResult result = processTransaction(record.value());

        
        kafkaTemplate.send(outputTopic, result.getKey(), result);
    }
}