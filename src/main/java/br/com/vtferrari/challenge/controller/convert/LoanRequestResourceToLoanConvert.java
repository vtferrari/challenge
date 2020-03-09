package br.com.vtferrari.challenge.controller.convert;

import br.com.vtferrari.challenge.controller.resource.LoanRequestResource;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.domain.Term;
import org.springframework.stereotype.Component;

@Component
public class LoanRequestResourceToLoanConvert {
    public Loan converter(LoanRequestResource loanRequestResource) {
        return Loan
                .builder()
                .name(loanRequestResource.getName())
                .cpf(loanRequestResource.getCpf())
                .birthdate(loanRequestResource.getBirthdate())
                .amount(loanRequestResource.getAmount())
                .terms(Term.fromValue(loanRequestResource.getTerms()))
                .income(loanRequestResource.getIncome())
                .build();
    }

}
