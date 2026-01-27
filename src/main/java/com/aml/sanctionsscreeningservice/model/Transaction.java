package com.aml.sanctionsscreeningservice.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Transaction(
        UUID transactionId,
        String externalTransactionId,
        String userId,
        BigDecimal amount,
        String currency,
        String merchantId,
        String country,
        Instant occuredAt
) {}
