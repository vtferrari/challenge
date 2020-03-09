package br.com.vtferrari.challenge.integration.gateway;

import br.com.vtferrari.challenge.integration.CommitmentIntegration;
import br.com.vtferrari.challenge.integration.resource.CommitmentIntegrationResource;
import br.com.vtferrari.challenge.integration.resource.ScoreIntegrationResource;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.gateway.CommitmentIntegrationGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class CommitmentIntegrationGatewayImpl implements CommitmentIntegrationGateway {
    private final CommitmentIntegration commitmentIntegration;

    @Override
    public Mono<Loan> commitment(Loan loan) {

        return commitmentIntegration.getCommitment(getIntegration(loan))
                .map(integration -> loan.toBuilder().commitment(integration.getCommitment()).build());
    }

    private CommitmentIntegrationResource getIntegration(Loan loan) {
        return CommitmentIntegrationResource.builder().cpf(loan.getCpf()).build();
    }
}
