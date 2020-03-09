package br.com.vtferrari.challenge.usecase.impl;

import br.com.vtferrari.challenge.usecase.ScorePolicyUseCase;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.gateway.ScoreIntegrationGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ScorePolicyUseCaseImpl implements ScorePolicyUseCase {

    private ScoreIntegrationGateway scoreIntegrationGateway;

    @Override
    public Mono<Loan> execute(Loan loan) {
        return Mono.just(loan)
                .flatMap(scoreIntegrationGateway::score)
                .map(this::getProcessedScoreLoan);
    }

    private Loan getProcessedScoreLoan(Loan process) {
        return process
                .toBuilder()
                .scorePolicy(isScore(process))
                .build();
    }

    private boolean isScore(Loan loan) {
        return loan.getScore() >= 600;
    }

}
