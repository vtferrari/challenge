package br.com.vtferrari.challenge.usecase.impl;

import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.gateway.LoanRepositoryGateway;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckLoanUseCaseImplTest {

    @InjectMocks
    private CheckLoanUseCaseImpl checkLoanUseCase;
    @Mock
    private LoanRepositoryGateway loanRepositoryGateway;

    @Test
    @DisplayName("Get loan to check status")
    void getLoanToCheckStatus() {
        final var loan = Loan.builder().score(600).build();
        when(loanRepositoryGateway.findById(any(Loan.class))).thenReturn(Mono.just(loan));
        final var execute = checkLoanUseCase.execute(loan).block();
        assertEquals(600, execute.getScore());

        verify(loanRepositoryGateway,never()).save(any(Loan.class));
        verify(loanRepositoryGateway,atLeastOnce()).findById(any(Loan.class));

    }

}