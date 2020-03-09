package br.com.vtferrari.challenge.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("loan")
public class LoanDatabase {
    private String id;
    private String name;
    private String cpf;
    private String status;
    private LocalDate birthdate;
    private BigDecimal amount;
    private Integer terms;
    private BigDecimal income;
    private boolean agePolicy;
    private boolean commitmentPolicy;
    private boolean scorePolicy;
    private BigDecimal commitment;
    private int score;
    private BigDecimal interestRate;
}
