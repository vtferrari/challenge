package br.com.vtferrari.challenge.usecase.gateway;

import br.com.vtferrari.challenge.usecase.domain.Loan;
import reactor.core.publisher.Mono;

public interface LoanQueueGateway {
    Mono<Loan> send(Loan loan);
}
