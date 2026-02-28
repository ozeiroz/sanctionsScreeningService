package com.aml.sanctionsscreeningservice.screening.service;

import com.aml.sanctionsscreeningservice.sanctions.model.SanctionedPerson;
import com.aml.sanctionsscreeningservice.screening.model.ScreeningResult;
import com.aml.sanctionsscreeningservice.screening.model.ScreeningStatus;
import com.aml.sanctionsscreeningservice.screening.model.Transaction;
import com.aml.sanctionsscreeningservice.sanctions.repository.SanctionedPersonRepository;
import com.aml.sanctionsscreeningservice.screening.repository.ScreeningRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class SanctionsScreeningService {
    private final KafkaTemplate<String, ScreeningResult> kafkaTemplate;
    private final String outputTopic;
    private final SanctionedPersonRepository sanctionedPersonRepository;
    private final ScreeningRepository screeningRepository;

    public SanctionsScreeningService(
            KafkaTemplate<String, ScreeningResult> kafkaTemplate,
            @Value("${kafka.topics.transactions.sanctions}") String outputTopic,
            SanctionedPersonRepository sanctionedPersonRepository,
            ScreeningRepository screeningRepository
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.outputTopic = outputTopic;
        this.sanctionedPersonRepository = sanctionedPersonRepository;
        this.screeningRepository = screeningRepository;
    }

    @Transactional("screeningTransactionManager")
    @KafkaListener(topics = "${kafka.topics.transactions.raw}")
    public void checkSanctions(ConsumerRecord<String, Transaction> record) {
        Transaction transaction = record.value();

        Optional<SanctionedPerson> person = sanctionedPersonRepository.findByFullName(transaction.fullName());
        ScreeningResult screenedPerson = new ScreeningResult();
        if (person.isPresent()) {
            screenedPerson.setStatus(ScreeningStatus.MATCH);
        }
        else {
            screenedPerson.setStatus(ScreeningStatus.CLEAR);
        }

        screenedPerson.setUserId(transaction.userId());
        screenedPerson.setExternalTransactionId(transaction.externalTransactionId());
        screenedPerson.setScreenedAt(Instant.now());
        screeningRepository.save(screenedPerson);

        screenedPerson.setScreeningId(screenedPerson.getScreeningId());

        kafkaTemplate.send(outputTopic, screenedPerson.getExternalTransactionId(), screenedPerson);
    }
}