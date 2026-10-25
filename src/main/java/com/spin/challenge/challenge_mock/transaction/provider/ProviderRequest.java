package com.spin.challenge.challenge_mock.transaction.provider;

import java.math.BigDecimal;

public record ProviderRequest(
        String accountId,
        String transactionId,
        String type,          // CREDIT | DEBIT
        BigDecimal amount,
        String currency) {}
