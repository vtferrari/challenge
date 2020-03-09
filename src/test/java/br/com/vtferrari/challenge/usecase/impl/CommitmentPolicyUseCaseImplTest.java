package br.com.vtferrari.challenge.usecase.impl;

import br.com.vtferrari.challenge.usecase.InstalmentCalcUseCase;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.gateway.CommitmentIntegrationGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommitmentPolicyUseCaseImplTest {
    @InjectMocks
    private CommitmentPolicyUseCaseImpl commitmentPolicyUseCase;
    @Mock
    private CommitmentIntegrationGateway commitmentIntegrationGateway;
    @Mock
    private InstalmentCalcUseCase instalmentCalcUseCase;


    @Test
    @DisplayName("Can commit with loan policy")
    void canCommitWithLoanPolicy() {
        final var loan = Loan.builder().score(600).build();
        when(commitmentIntegrationGateway.commitment(any(Loan.class))).thenReturn(Mono.just(loan));
        when(instalmentCalcUseCase.execute(any(Loan.class))).thenReturn(Mono.just(loan));
        final var execute = commitmentPolicyUseCase.execute(loan).block();
        assertEquals(600, execute.getScore());

        verify(instalmentCalcUseCase,atLeastOnce()).execute(any(Loan.class));
        verify(commitmentIntegrationGateway,atLeastOnce()).commitment(any(Loan.class));
    }
}