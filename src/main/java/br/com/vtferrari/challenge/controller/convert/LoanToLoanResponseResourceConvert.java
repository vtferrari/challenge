package br.com.vtferrari.challenge.controller.convert;

import br.com.vtferrari.challenge.controller.resource.LoanResponseResource;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import org.springframework.stereotype.Component;

import static br.com.vtferrari.challenge.usecase.domain.Status.PROCESSING;
import static java.util.Objects.isNull;

@Component
public class LoanToLoanResponseResourceConvert {
    public LoanResponseResource converter(Loan loan) {
        if (isNull(refusedPolicy(loan))) {
            return LoanResponseResource
                    .builder()
                    .id(loan.getId())
                    .status(loan.getStatus().getName())
                    .result(this.result(loan))
                    .refusedPolicy(this.refusedPolicy(loan))
                    .amount(loan.getAmount())
                    .terms(loan.getTerms().getTerms())
                    .build();
        }
        return LoanResponseResource
                .builder()
                .id(loan.getId())
                .status(loan.getStatus().getName())
                .result(this.result(loan))
                .refusedPolicy(this.refusedPolicy(loan))
                .build();
    }

    private String result(Loan loan) {
        if (PROCESSING.equals(loan.getStatus())) {
            return null;
        }
        if (isNull(refusedPolicy(loan))) {
            return "approved";
        }
        return "refused";
    }


    private String refusedPolicy(Loan loan) {
        if (!loan.isScorePolicy()) {
            return "SCORE";
        }
        if (!loan.isAgePolicy()) {
            return "AGE";
        }
        if (!loan.isCommitmentPolicy()) {
            return "COMMITMENT";
        }
        return null;
    }
}
