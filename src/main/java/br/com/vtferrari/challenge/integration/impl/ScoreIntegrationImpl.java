package br.com.vtferrari.challenge.integration.impl;

import br.com.vtferrari.challenge.integration.ScoreIntegration;
import br.com.vtferrari.challenge.integration.resource.ScoreIntegrationResource;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ScoreIntegrationImpl implements ScoreIntegration {
    public static final String X_API_KEY = "x-api-key";
    public static final String X_API_KEY_TOKEN = "z2qcDsl6BK8FEPynp2ND17WvcJKMQTpjT5lcyQ0d";
    private final WebClient webClient;

    @Override
    public Mono<ScoreIntegrationResource> getScore(ScoreIntegrationResource scoreIntegrationResource) {

        return webClient
                .post()
                .uri("/score")
                .header(X_API_KEY, X_API_KEY_TOKEN)
                .body(Mono.just(scoreIntegrationResource), ScoreIntegrationResource.class)
                .retrieve()
                .bodyToMono(ScoreIntegrationResource.class);
    }
}
