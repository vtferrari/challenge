package br.com.vtferrari.challenge.usecase.gateway;

import br.com.vtferrari.challenge.usecase.domain.Loan;
import reactor.core.publisher.Mono;

public interface LoanRepositoryGateway {
    Mono<Loan> save(Loan loan);

    Mono<Loan> findById(Loan loan);
}
