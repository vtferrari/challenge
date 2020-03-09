package br.com.vtferrari.challenge.usecase.impl;

import br.com.vtferrari.challenge.usecase.InstalmentCalcUseCase;
import br.com.vtferrari.challenge.usecase.InterestRateUseCase;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static br.com.vtferrari.challenge.usecase.domain.Term.*;

@Component
@AllArgsConstructor
public class InstalmentCalcUseCaseImpl implements InstalmentCalcUseCase {
    private final InterestRateUseCase interestRateUseCase;

    @Override
    public Mono<Loan> execute(Loan loan) {
        return Mono.just(loan)
                .flatMap(interestRateUseCase::execute)
                .map(this::commitment);
    }


    private Double calcAmount(Loan loan, int terms) {
        final var amount = loan.getAmount();
        final var i = loan.getInterestRate().floatValue();
        final var pt1 = Math.pow(1 + i, terms) * i;
        final var pt2 = Math.pow(1 + i, terms) - 1;
        return amount.floatValue() * (pt1 / pt2);
    }

    private Loan commitment(Loan loan) {
        final var commitment = loan.getCommitment();
        final var loanCommitted = loan.getIncome().doubleValue() -commitment.doubleValue() * loan.getIncome().doubleValue() ;
                System.out.println(loanCommitted);
        switch (loan.getTerms().getTerms()) {
            case 6:
                final var calcAmountSix = calcAmount(loan, SIX.getTerms());
                System.out.println(calcAmountSix);
                final var commitmentSix = calcAmountSix < loanCommitted;
                if (commitmentSix) {
                    return loan.toBuilder().terms(SIX).commitmentPolicy(true).build();
                }
            case 9:
                final var calcAmountNine = calcAmount(loan, NINE.getTerms());
                System.out.println(calcAmountNine);
                final var commitmentNine = calcAmountNine < loanCommitted;
                if (commitmentNine) {
                    return loan.toBuilder().terms(NINE).commitmentPolicy(true).build();
                }
            case 12:
                final var calcAmountTwelve = calcAmount(loan, TWELVE.getTerms());
                System.out.println(calcAmountTwelve);
                final var commitmentTwelve = calcAmountTwelve < loanCommitted;
                if (commitmentTwelve) {
                    return loan.toBuilder().terms(TWELVE).commitmentPolicy(true).build();
                }
            default:
                return loan.toBuilder().commitmentPolicy(false).build();
        }
    }
}
