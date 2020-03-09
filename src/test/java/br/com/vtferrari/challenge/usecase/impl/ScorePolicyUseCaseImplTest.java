package br.com.vtferrari.challenge.usecase.impl;

import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.gateway.ScoreIntegrationGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScorePolicyUseCaseImplTest {

    @InjectMocks
    private ScorePolicyUseCaseImpl scorePolicyUseCase;
    @Mock
    private ScoreIntegrationGateway scoreIntegrationGateway;

    @Test
    void scoreGraterThe600() {
        final var loan = Loan.builder().score(600).build();
        when(scoreIntegrationGateway.score(any(Loan.class))).thenReturn(Mono.just(loan));
        final var execute = scorePolicyUseCase.execute(loan).block();
        assertEquals(600, execute.getScore());
        assertTrue(execute.isScorePolicy());
    }

    @Test
    void scoreLowerThe600() {
        final var loan = Loan.builder().score(599).build();
        when(scoreIntegrationGateway.score(any(Loan.class))).thenReturn(Mono.just(loan));
        final var execute = scorePolicyUseCase.execute(loan).block();
        assertEquals(599, execute.getScore());
        assertFalse(execute.isScorePolicy());
    }
}