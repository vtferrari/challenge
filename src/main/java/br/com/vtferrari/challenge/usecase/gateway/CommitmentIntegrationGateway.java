package br.com.vtferrari.challenge.usecase.gateway;

import br.com.vtferrari.challenge.usecase.domain.Loan;
import reactor.core.publisher.Mono;

public interface CommitmentIntegrationGateway {
    Mono<Loan> commitment(Loan loan);

}
