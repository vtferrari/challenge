package br.com.vtferrari.challenge.usecase.impl;

import br.com.vtferrari.challenge.usecase.CreateLoanAndSendToQueueUseCase;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.gateway.LoanQueueGateway;
import br.com.vtferrari.challenge.usecase.gateway.LoanRepositoryGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CreateLoanAndSendToQueueUseCaseImpl implements CreateLoanAndSendToQueueUseCase {
    private final LoanRepositoryGateway loanRepositoryGateway;
    private final LoanQueueGateway loanQueueGateway;

    @Override
    public Mono<Loan> execute(Loan loan) {
        return loanRepositoryGateway.save(loan).doOnNext(loanQueueGateway::send);
    }
}
