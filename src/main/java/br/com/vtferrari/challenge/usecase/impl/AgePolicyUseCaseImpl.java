package br.com.vtferrari.challenge.usecase.impl;

import br.com.vtferrari.challenge.usecase.AgePolicyUseCase;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.Period;

@Slf4j
@Component
public class AgePolicyUseCaseImpl implements AgePolicyUseCase {
    @Override
    public Mono<Loan> execute(Loan loan) {

        return Mono.just(loan)
                .map(this::getProcessedAgeLoan);
    }

    private Loan getProcessedAgeLoan(Loan loan) {
        log.info("Loan age is {}",Period.between(loan.getBirthdate(), LocalDate.now()).getYears());
        return loan
                .toBuilder()
                .agePolicy(isOlder(loan))
                .build();
    }

    private boolean isOlder(Loan loan) {
        return  Period.between(loan.getBirthdate(), LocalDate.now()).getYears() >= 18;
    }
}
