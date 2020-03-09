package br.com.vtferrari.challenge.usecase.impl;

import br.com.vtferrari.challenge.usecase.InterestRateUseCase;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
public class InterestRateUseCaseImpl implements InterestRateUseCase {
    @Override
    public Mono<Loan> execute(Loan loan) {
        return Mono.just(loan)
                .map(this::getLoanWithInterestRate);
    }

    private Loan getLoanWithInterestRate(Loan loan) {
        final var score = loan.getScore();
        final var terms = loan.getTerms();
        return loan
                .toBuilder()
                .interestRate(BigDecimal.valueOf(terms.getByScore(score)/100))
                .build();
    }
}
