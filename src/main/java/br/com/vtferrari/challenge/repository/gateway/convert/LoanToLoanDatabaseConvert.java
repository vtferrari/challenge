package br.com.vtferrari.challenge.repository.gateway.convert;

import br.com.vtferrari.challenge.repository.model.LoanDatabase;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.domain.Status;
import org.springframework.stereotype.Component;

@Component
public class LoanToLoanDatabaseConvert {
    public LoanDatabase converter(Loan loan) {
        return LoanDatabase
                .builder()
                .id(loan.getId())
                .name(loan.getName())
                .cpf(loan.getCpf())
                .status(loan.getStatus().getName())
                .birthdate(loan.getBirthdate())
                .amount(loan.getAmount())
                .terms(loan.getTerms().getTerms())
                .income(loan.getIncome())
                .agePolicy(loan.isAgePolicy())
                .commitmentPolicy(loan.isCommitmentPolicy())
                .scorePolicy(loan.isScorePolicy())
                .commitment(loan.getCommitment())
                .score(loan.getScore())
                .interestRate(loan.getInterestRate())
                .build();
    }
}
