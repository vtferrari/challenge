package br.com.vtferrari.challenge.integration.gateway;

import br.com.vtferrari.challenge.integration.ScoreIntegration;
import br.com.vtferrari.challenge.integration.resource.CommitmentIntegrationResource;
import br.com.vtferrari.challenge.integration.resource.ScoreIntegrationResource;
import br.com.vtferrari.challenge.usecase.domain.Loan;
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
class ScoreIntegrationGatewayImplTest {
    @InjectMocks
    private ScoreIntegrationGatewayImpl scoreIntegrationGateway;
    @Mock
    private ScoreIntegration scoreIntegration;

    @Test
    @DisplayName("Apply score rules")
    void applyScoreRules() {
        final var loan = Loan.builder().id("123").score(600).build();
        when(scoreIntegration.getScore(any(ScoreIntegrationResource.class))).thenReturn(Mono.just(ScoreIntegrationResource.builder().score(600).build()));
        final var execute = scoreIntegrationGateway.score(loan).block();
        assertEquals(600, execute.getScore());

        verify(scoreIntegration,atLeastOnce()).getScore(any(ScoreIntegrationResource.class)) ;
    }
}