package com.spin.challenge.challenge_mock.transaction.provider;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProviderResponse(String providerReference,
                               String transactionId,
                               BigDecimal balance,
                               LocalDateTime executedAt,
                               String status,// APPROVED | REJECTED
                               String code,
                               String message) {
}
