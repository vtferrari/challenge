package br.com.vtferrari.challenge.controller.convert;

import br.com.vtferrari.challenge.usecase.domain.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanIdToLoanConvert {
    public Loan converter(String id) {
        return Loan.builder().id(id).build();
    }
}
