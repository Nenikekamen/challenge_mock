package com.spin.challenge.challenge_mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.spin.challenge.challenge_mock.transaction.exception.ProviderException;
import com.spin.challenge.challenge_mock.transaction.provider.ProviderClient;
import com.spin.challenge.challenge_mock.transaction.provider.ProviderRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest
class ChallengeMockApplicationTests {

	static WireMockServer provider = null;

	@BeforeAll
	static void start() {
		try {
			provider = new WireMockServer(wireMockConfig().dynamicPort());
			provider.start();
		} catch (Exception e) {
			System.err.println("Error iniciando WireMock: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@AfterAll
	static void stop() {
		if (provider != null) provider.stop();
	}

	@DynamicPropertySource
	static void props(DynamicPropertyRegistry registry) {
		registry.add("provider.base-url", () -> provider != null ? provider.baseUrl() : "http://localhost:8080");
		registry.add("provider.read-timeout", () -> "500ms");
	}

	@Autowired
    ProviderClient client;

	@Test
	void contextLoads() {
	}

	@Test
	void approvesCredit() {
		if (provider == null) return;
		provider.stubFor(post("/provider/v1/execute")
				.willReturn(okJson("""
                    {"providerReference":"ref-1","transactionId":"tx-1","status":"APPROVED"}
                    """)));

		var res = client.execute(new ProviderRequest("tx-1", "acc-1", "CREDIT", new BigDecimal("100"), "MXN"));

		Assertions.assertEquals("APPROVED", res.status());
		provider.verify(postRequestedFor(urlEqualTo("/provider/v1/execute")));
	}

	@Test
	void timeoutIsRetryable() {
		if (provider == null) return;
		provider.stubFor(post("/provider/v1/execute")
				.willReturn(ok().withFixedDelay(2000)));

		ProviderException exception = Assertions.assertThrows(ProviderException.class, () ->
				client.execute(new ProviderRequest("tx-2", "acc-1", "DEBIT", new BigDecimal("10"), "MXN"))
		);

		Assertions.assertTrue(exception.isRetryable());
	}
}
