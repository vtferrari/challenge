package br.com.vtferrari.challenge.integration.gateway;

import br.com.vtferrari.challenge.integration.CommitmentIntegration;
import br.com.vtferrari.challenge.integration.resource.CommitmentIntegrationResource;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CommitmentIntegrationGatewayImplTest {
    @InjectMocks
    private CommitmentIntegrationGatewayImpl commitmentIntegrationGateway;
    @Mock
    private CommitmentIntegration commitmentIntegration;

    @Test
    @DisplayName("Apply commitment rules")
    void applyCommitmentRules() {
        final var loan = Loan.builder().id("123").score(600).build();
        when(commitmentIntegration.getCommitment(any(CommitmentIntegrationResource.class))).thenReturn(Mono.just(CommitmentIntegrationResource.builder().commitment(BigDecimal.ONE).build()));
        final var execute = commitmentIntegrationGateway.commitment(loan).block();
        assertEquals(BigDecimal.ONE, execute.getCommitment());

        verify(commitmentIntegration,atLeastOnce()).getCommitment(any(CommitmentIntegrationResource.class)) ;
    }
}