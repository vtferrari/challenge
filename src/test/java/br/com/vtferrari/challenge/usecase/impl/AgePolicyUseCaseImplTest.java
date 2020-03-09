package br.com.vtferrari.challenge.usecase.impl;

import br.com.vtferrari.challenge.usecase.domain.Loan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AgePolicyUseCaseImplTest {
    @InjectMocks
    private AgePolicyUseCaseImpl agePolicyUseCaseImpl;

    @Test
    void policyFalse() {
        final var loan = Loan.builder().birthdate(LocalDate.of(2020,10,20)).build();
        final var execute = agePolicyUseCaseImpl.execute(loan).block();
        assertFalse(execute.isAgePolicy());
    }
    @Test
    void policyTrue() {
        final var loan = Loan.builder().birthdate(LocalDate.of(1989,10,20)).build();
        final var execute = agePolicyUseCaseImpl.execute(loan).block();
        assertTrue(execute.isAgePolicy());
    }
}