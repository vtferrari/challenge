package br.com.vtferrari.challenge.integration;

import br.com.vtferrari.challenge.integration.resource.ScoreIntegrationResource;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface ScoreIntegration {
    Mono<ScoreIntegrationResource> getScore(ScoreIntegrationResource cpf);
}
