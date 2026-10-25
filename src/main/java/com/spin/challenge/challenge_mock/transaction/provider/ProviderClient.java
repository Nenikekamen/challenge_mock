package com.spin.challenge.challenge_mock.transaction.provider;

import com.spin.challenge.challenge_mock.transaction.exception.ProviderException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@RequiredArgsConstructor
public class ProviderClient {

    private final RestClient providerRestClient;

    public ProviderResponse execute(ProviderRequest request) {
        try {
            return providerRestClient.post()
                    .uri("/provider/v1/execute")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Idempotency-Key", request.transactionId())
                    .body(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        throw new ProviderException("Rechazada por proveedor: " + res.getStatusCode(), false);
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw new ProviderException("Error del proveedor: " + res.getStatusCode(), true);
                    })
                    .toEntity(ProviderResponse.class)
                    .getBody();
        } catch (RestClientException e) {   // timeouts / conexión / errores de parsing
            if (e.getCause() instanceof ProviderException pe) {
                throw pe;
            }
            throw new ProviderException("Proveedor no disponible: " + e.getMessage(), true, e);
        }
    }
}