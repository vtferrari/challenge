package br.com.vtferrari.challenge.integration.impl;

import br.com.vtferrari.challenge.integration.resource.CommitmentIntegrationResource;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CommitmentIntegrationImplTest {
    @InjectMocks
    private CommitmentIntegrationImpl commitmentIntegrationImpl;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Test
    @DisplayName("Get commitment from web")
    void getCommitmentFromWeb() {
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(anyString(), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(any(), eq(CommitmentIntegrationResource.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        final var commitmentIntegrationResource = CommitmentIntegrationResource.builder().commitment(BigDecimal.ONE).build();
        when(responseSpec.bodyToMono(eq(CommitmentIntegrationResource.class))).thenReturn(Mono.just(commitmentIntegrationResource));
        final var execute = commitmentIntegrationImpl.getCommitment(commitmentIntegrationResource).block();
        assertEquals(BigDecimal.ONE, execute.getCommitment());
    }
}