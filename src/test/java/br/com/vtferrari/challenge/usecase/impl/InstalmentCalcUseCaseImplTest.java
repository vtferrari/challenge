package br.com.vtferrari.challenge.usecase.impl;

import br.com.vtferrari.challenge.usecase.InterestRateUseCase;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.domain.Term;
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
class InstalmentCalcUseCaseImplTest {
    @InjectMocks
    private InstalmentCalcUseCaseImpl instalmentCalcUseCase;
    @Mock
    private InterestRateUseCase interestRateUseCase;

    @Test
    void save() {
        final var loan = Loan
                .builder()
                .score(600)
                .terms(Term.SIX)
                .amount(BigDecimal.valueOf(1000))
                .income(BigDecimal.valueOf(1000))
                .commitment(BigDecimal.valueOf(1))
                .interestRate(BigDecimal.valueOf(0.069))
                .build();
        when(interestRateUseCase.execute(any(Loan.class))).thenReturn(Mono.just(loan));
        final var execute = instalmentCalcUseCase.execute(loan).block();
        assertEquals(600, execute.getScore());

        assertEquals(Term.SIX, execute.getTerms());
        assertFalse(execute.isCommitmentPolicy());
    }

    @Test
    void save2() {
        final var loan = Loan
                .builder()
                .score(900)
                .terms(Term.SIX)
                .amount(BigDecimal.valueOf(1000))
                .income(BigDecimal.valueOf(1500))
                .commitment(BigDecimal.valueOf(0.01))
                .interestRate(BigDecimal.valueOf(0.069))
                .build();
        when(interestRateUseCase.execute(any(Loan.class))).thenReturn(Mono.just(loan));

        final var execute = instalmentCalcUseCase.execute(loan).block();
        assertEquals(900, execute.getScore());
        assertEquals(Term.SIX, execute.getTerms());
        assertTrue(execute.isCommitmentPolicy());
    }

    @Test
    void save3() {
        final var loan = Loan
                .builder()
                .score(900)
                .terms(Term.SIX)
                .amount(BigDecimal.valueOf(1000))
                .income(BigDecimal.valueOf(1500))
                .commitment(BigDecimal.valueOf(0.88))
                .interestRate(BigDecimal.valueOf(0.069))
                .build();
        when(interestRateUseCase.execute(any(Loan.class))).thenReturn(Mono.just(loan));

        final var execute = instalmentCalcUseCase.execute(loan).block();
        assertEquals(900, execute.getScore());
        assertEquals(Term.NINE, execute.getTerms());
        assertTrue(execute.isCommitmentPolicy());
    }
    @Test
    void save4() {
        final var loan = Loan
                .builder()
                .score(900)
                .terms(Term.SIX)
                .amount(BigDecimal.valueOf(1000))
                .income(BigDecimal.valueOf(1500))
                .commitment(BigDecimal.valueOf(0.90))
                .interestRate(BigDecimal.valueOf(0.069))
                .build();
        when(interestRateUseCase.execute(any(Loan.class))).thenReturn(Mono.just(loan));

        final var execute = instalmentCalcUseCase.execute(loan).block();
        assertEquals(900, execute.getScore());
        assertEquals(Term.TWELVE, execute.getTerms());
        assertTrue(execute.isCommitmentPolicy());
    }
}