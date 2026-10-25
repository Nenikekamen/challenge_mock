package com.spin.challenge.challenge_mock.transaction.exception;

import lombok.Getter;

@Getter
public class ProviderException extends RuntimeException {

    private final boolean retryable;

    public ProviderException(String message, boolean retryable) {
        super(message);
        this.retryable = retryable;
    }

    public ProviderException(String message, boolean retryable, Throwable cause) {
        super(message, cause);
        this.retryable = retryable;
    }
}
