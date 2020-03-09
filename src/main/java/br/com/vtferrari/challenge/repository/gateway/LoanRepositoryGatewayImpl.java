package br.com.vtferrari.challenge.repository.gateway;

import br.com.vtferrari.challenge.repository.LoanRepository;
import br.com.vtferrari.challenge.repository.gateway.convert.LoanDatabaseToLoanConvert;
import br.com.vtferrari.challenge.repository.gateway.convert.LoanToLoanDatabaseConvert;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.gateway.LoanRepositoryGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class LoanRepositoryGatewayImpl implements LoanRepositoryGateway {
    private final LoanRepository loanRepository;
    private final LoanToLoanDatabaseConvert loanToLoanDatabaseConvert;
    private final LoanDatabaseToLoanConvert loanDatabaseToLoanConvert;

    @Override
    public Mono<Loan> save(Loan loan) {

        final var loanDatabase = loanToLoanDatabaseConvert.converter(loan);
        return loanRepository.save(loanDatabase)
                .map(loanDatabaseToLoanConvert::converter);
    }

    @Override
    public Mono<Loan> findById(Loan loan) {
        return loanRepository.findById(loan.getId())
                .map(loanDatabaseToLoanConvert::converter);
    }
}
