package br.com.vtferrari.challenge.integration;

import br.com.vtferrari.challenge.integration.resource.CommitmentIntegrationResource;
import br.com.vtferrari.challenge.integration.resource.ScoreIntegrationResource;
import reactor.core.publisher.Mono;

public interface CommitmentIntegration {
    Mono<CommitmentIntegrationResource> getCommitment(CommitmentIntegrationResource cpf);
}
