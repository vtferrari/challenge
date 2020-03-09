package br.com.vtferrari.challenge.repository.gateway.convert;

import br.com.vtferrari.challenge.repository.model.LoanDatabase;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.domain.Status;
import br.com.vtferrari.challenge.usecase.domain.Term;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

import static br.com.vtferrari.challenge.usecase.domain.Status.PROCESSING;

@Component
public class LoanDatabaseToLoanConvert {
    public Loan converter(LoanDatabase loanDatabase) {
        final var terms = Term.fromValue(String.valueOf(loanDatabase.getTerms()));
        return Loan
                .builder()
                .id(loanDatabase.getId())
                .name(loanDatabase.getName())
                .cpf(loanDatabase.getCpf())
                .status(Status.fromValue(loanDatabase.getStatus()))
                .birthdate(loanDatabase.getBirthdate())
                .amount(loanDatabase.getAmount())
                .terms(terms)
                .income(loanDatabase.getIncome())
                .agePolicy(loanDatabase.isAgePolicy())
                .commitmentPolicy(loanDatabase.isCommitmentPolicy())
                .scorePolicy(loanDatabase.isScorePolicy())
                .commitment(loanDatabase.getCommitment())
                .score(loanDatabase.getScore())
                .interestRate(loanDatabase.getInterestRate())
                .build();

    }
}
