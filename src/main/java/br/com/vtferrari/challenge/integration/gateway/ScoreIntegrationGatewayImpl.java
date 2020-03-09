package br.com.vtferrari.challenge.integration.gateway;

import br.com.vtferrari.challenge.integration.ScoreIntegration;
import br.com.vtferrari.challenge.integration.resource.ScoreIntegrationResource;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.gateway.ScoreIntegrationGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ScoreIntegrationGatewayImpl implements ScoreIntegrationGateway {
    private final ScoreIntegration scoreIntegration;

    @Override
    public Mono<Loan> score(Loan loan) {

        return scoreIntegration.getScore(getIntegration(loan))
                .map(integration -> loan.toBuilder().score(integration.getScore()).build());
    }

    private ScoreIntegrationResource getIntegration(Loan loan) {
        return ScoreIntegrationResource.builder().cpf(loan.getCpf()).build();
    }
}
