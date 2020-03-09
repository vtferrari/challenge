package br.com.vtferrari.challenge.usecase.impl;

import br.com.vtferrari.challenge.usecase.CommitmentPolicyUseCase;
import br.com.vtferrari.challenge.usecase.InstalmentCalcUseCase;
import br.com.vtferrari.challenge.usecase.InterestRateUseCase;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.gateway.CommitmentIntegrationGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CommitmentPolicyUseCaseImpl implements CommitmentPolicyUseCase {

    private final CommitmentIntegrationGateway commitmentIntegrationGateway;
    private final InstalmentCalcUseCase instalmentCalcUseCase;


    @Override
    public Mono<Loan> execute(Loan loan) {
        return Mono.just(loan)
                .flatMap(commitmentIntegrationGateway::commitment)
                .flatMap(instalmentCalcUseCase::execute);
    }

}
