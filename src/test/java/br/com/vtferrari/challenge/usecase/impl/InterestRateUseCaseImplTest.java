package br.com.vtferrari.challenge.usecase.impl;

import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.domain.Term;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class InterestRateUseCaseImplTest {

    @InjectMocks
    private InterestRateUseCaseImpl interestRateUseCaseImpl;

    @Test
    @DisplayName("Get Interest Rate to score 600")
    void calcInterestRate() {
        final var loan = Loan.builder().terms(Term.TWELVE).score(600).build();
        final var execute = interestRateUseCaseImpl.execute(loan).block();
        assertEquals(BigDecimal.valueOf(0.069), execute.getInterestRate());
    }

}