package br.com.vtferrari.challenge.controller.convert;

import br.com.vtferrari.challenge.controller.resource.LoanTokenResponseResource;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LoanToLoanTokenResponseResourceConvert {
    public LoanTokenResponseResource converter(Loan loan) {
        return LoanTokenResponseResource
                .builder()
                .id(loan.getId())
                .build();
    }
}
