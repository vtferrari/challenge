package br.com.vtferrari.challenge.usecase.impl;

import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.domain.Term;
import br.com.vtferrari.challenge.usecase.gateway.ScoreIntegrationGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class InterestRateUseCaseImplTest {

    @InjectMocks
    private InterestRateUseCaseImpl interestRateUseCaseImpl ;

    @Test
    void calc() {
        final var loan = Loan.builder().terms(Term.TWELVE).score(600).build();
        final var execute = interestRateUseCaseImpl.execute(loan).block();
        assertEquals(BigDecimal.valueOf(0.069), execute.getInterestRate());
    }

}