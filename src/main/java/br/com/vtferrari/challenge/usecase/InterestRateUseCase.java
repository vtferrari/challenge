package br.com.vtferrari.challenge.usecase;

import br.com.vtferrari.challenge.usecase.domain.Loan;
import reactor.core.publisher.Mono;

public interface InterestRateUseCase {
    Mono<Loan> execute(Loan loan);
}
