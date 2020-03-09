package br.com.vtferrari.challenge.controller.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanRequestResource {
    @NotBlank
    private String name;
    @NotBlank
    private String cpf;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    @NotNull
    @Min(1000)
    @Max(4000)
    private BigDecimal amount;
    @NotNull
    @Pattern(regexp = "^(6|9|12)$")
    private String terms;
    @NotNull
    private BigDecimal income;
}
