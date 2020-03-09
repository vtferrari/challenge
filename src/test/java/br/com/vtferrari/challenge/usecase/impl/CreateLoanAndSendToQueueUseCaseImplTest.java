package br.com.vtferrari.challenge.usecase.impl;

import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.gateway.LoanQueueGateway;
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
class CreateLoanAndSendToQueueUseCaseImplTest {
    @InjectMocks
    private CreateLoanAndSendToQueueUseCaseImpl createLoanAndSendToQueueUseCase;
    @Mock
    private LoanRepositoryGateway loanRepositoryGateway;
    @Mock
    private LoanQueueGateway loanQueueGateway;

    @Test
    @DisplayName("Send loan to queue and save")
    void sendLoanToQueueAndSave() {
        final var loan = Loan.builder().score(600).build();
        when(loanRepositoryGateway.save(any(Loan.class))).thenReturn(Mono.just(loan));
        when(loanQueueGateway.send(any(Loan.class))).thenReturn(Mono.just(loan));
        final var execute = createLoanAndSendToQueueUseCase.execute(loan).block();
        assertEquals(600, execute.getScore());

        verify(loanRepositoryGateway,atLeastOnce()).save(any(Loan.class));
        verify(loanQueueGateway,atLeastOnce()).send(any(Loan.class));
    }
}