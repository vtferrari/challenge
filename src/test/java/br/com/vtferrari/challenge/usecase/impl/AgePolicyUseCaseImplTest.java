package br.com.vtferrari.challenge.usecase.impl;

import br.com.vtferrari.challenge.usecase.domain.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
class AgePolicyUseCaseImplTest {
    @InjectMocks
    private AgePolicyUseCaseImpl agePolicyUseCaseImpl;

    @Test
    @DisplayName("Age policy is false")
    void validateAgePolicyFalse() {
        final var loan = Loan.builder().birthdate(LocalDate.of(2020, 10, 20)).build();
        final var execute = agePolicyUseCaseImpl.execute(loan).block();
        assertFalse(execute.isAgePolicy());
    }

    @Test
    @DisplayName("Age policy is true")
    void validateAgePolicyTrue() {
        final var loan = Loan.builder().birthdate(LocalDate.of(1989, 10, 20)).build();
        final var execute = agePolicyUseCaseImpl.execute(loan).block();
        assertTrue(execute.isAgePolicy());

    }
}