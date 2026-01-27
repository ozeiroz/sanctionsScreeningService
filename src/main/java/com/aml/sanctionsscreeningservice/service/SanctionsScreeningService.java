package com.aml.sanctionsscreeningservice.service;

import com.aml.sanctionsscreeningservice.model.SanctionedPerson;
import com.aml.sanctionsscreeningservice.model.ScreeningResult;
import com.aml.sanctionsscreeningservice.model.ScreeningStatus;
import com.aml.sanctionsscreeningservice.model.Transaction;
import com.aml.sanctionsscreeningservice.repository.SanctionedPersonRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

@Service
public class SanctionsScreeningService {
    private final KafkaTemplate<String, ScreeningResult> kafkaTemplate;
    private final String outputTopic;
    private SanctionedPersonRepository sanctionedPersonRepository;

    public SanctionsScreeningService(
            KafkaTemplate<String, ScreeningResult> kafkaTemplate,
            @Value("${kafka.topics.transactions.sanctions}") String outputTopic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.outputTopic = outputTopic;
    }

    @KafkaListener(topics = "${kafka.topics.transactions.raw}")
    public void checkSanctions(ConsumerRecord<String, Transaction> record) {
        ScreeningResult result = processTransaction(record.value());

        //TODO - add to DB + generation of screeningId in DB

        kafkaTemplate.send(outputTopic, result.getExternalTransactionId(), result);
    }

    private ScreeningResult processTransaction(Transaction transaction) {
        Optional<SanctionedPerson> person = sanctionedPersonRepository.findByUserId(transaction.userId());
        Random random = new Random();
        ScreeningResult screenedPerson = new ScreeningResult();
        screenedPerson.setUserId(transaction.userId());
        screenedPerson.setExternalTransactionId(transaction.externalTransactionId());
        screenedPerson.setScreeningId(screenedPerson.getUserId() + random.nextInt(100)); //TODO TEMPORARY
        screenedPerson.setScreenedAt(Instant.now());

        if (person.isPresent()) {
            screenedPerson.setStatus(ScreeningStatus.MATCH);
        }
        else {
            screenedPerson.setStatus(ScreeningStatus.CLEAR);
        }

        return screenedPerson;
    }
}