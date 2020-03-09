package br.com.vtferrari.challenge.usecase.impl;

import br.com.vtferrari.challenge.usecase.CheckLoanUseCase;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.gateway.LoanRepositoryGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CheckLoanUseCaseImpl implements CheckLoanUseCase {
    private final LoanRepositoryGateway loanRepositoryGateway;

    @Override
    public Mono<Loan> execute(Loan loan) {
        return loanRepositoryGateway.findById(loan);
    }
}
