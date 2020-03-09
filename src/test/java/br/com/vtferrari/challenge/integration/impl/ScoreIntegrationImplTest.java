package br.com.vtferrari.challenge.integration.impl;

import br.com.vtferrari.challenge.integration.resource.ScoreIntegrationResource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ScoreIntegrationImplTest {

    @InjectMocks
    private ScoreIntegrationImpl scoreIntegration;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Test
    @DisplayName("Get score from web")
    void getScoreFromWeb() {
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(anyString(), anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(any(), eq(ScoreIntegrationResource.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        final var commitmentIntegrationResource = ScoreIntegrationResource.builder().score(600).build();
        when(responseSpec.bodyToMono(eq(ScoreIntegrationResource.class))).thenReturn(Mono.just(commitmentIntegrationResource));
        final var execute = scoreIntegration.getScore(commitmentIntegrationResource).block();
        assertEquals(600, execute.getScore());
    }
}