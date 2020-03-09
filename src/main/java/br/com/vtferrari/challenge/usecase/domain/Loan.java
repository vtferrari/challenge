package br.com.vtferrari.challenge.usecase.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static br.com.vtferrari.challenge.usecase.domain.Status.PROCESSING;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    private String id;
    private String name;
    private String cpf;
    @Builder.Default
    private Status status = PROCESSING;
    private LocalDate birthdate;
    private BigDecimal amount;
    private Term terms;
    private BigDecimal income;
    private boolean agePolicy;
    private boolean commitmentPolicy;
    private boolean scorePolicy;
    private BigDecimal commitment;
    private int score;
    private BigDecimal interestRate;
}
