package com.spin.challenge.challenge_mock.transaction.provider;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "provider")
public record ProviderProperties(String baseUrl, Duration connectTimeout, Duration readTimeout) {}
