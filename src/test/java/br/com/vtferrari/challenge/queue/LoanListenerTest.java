package br.com.vtferrari.challenge.queue;

import br.com.vtferrari.challenge.repository.model.LoanDatabase;
import br.com.vtferrari.challenge.usecase.AgePolicyUseCase;
import br.com.vtferrari.challenge.usecase.CommitmentPolicyUseCase;
import br.com.vtferrari.challenge.usecase.ScorePolicyUseCase;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.gateway.LoanRepositoryGateway;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanListenerTest {
    @InjectMocks
    private LoanListener loanListener;
    @Mock
    private ScorePolicyUseCase scorePolicyUseCase;
    @Mock
    private AgePolicyUseCase agePolicyUseCase;
    @Mock
    private CommitmentPolicyUseCase commitmentPolicyUseCase;
    @Mock
    private LoanRepositoryGateway loanRepositoryGateway;
    @Spy
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Start loan process")
    void startLoanProcess() throws JsonProcessingException {
        final var loan = Loan.builder().id("123").score(600).build();
        when(scorePolicyUseCase.execute(any(Loan.class))).thenReturn(Mono.just(loan));
        when(agePolicyUseCase.execute(any(Loan.class))).thenReturn(Mono.just(loan));
        when(commitmentPolicyUseCase.execute(any(Loan.class))).thenReturn(Mono.just(loan));
        when(loanRepositoryGateway.save(any(Loan.class))).thenReturn(Mono.just(loan));
        loanListener.process("{\"id\":\"123\"}");

        verify(scorePolicyUseCase,atLeastOnce()).execute(any(Loan.class));
        verify(agePolicyUseCase,atLeastOnce()).execute(any(Loan.class));
        verify(commitmentPolicyUseCase,atLeastOnce()).execute(any(Loan.class));
        verify(loanRepositoryGateway,atLeastOnce()).save(any(Loan.class));
        verify(objectMapper,atLeastOnce()).readValue(anyString(),eq(Loan.class));
    }

    @Test
    @DisplayName("Loan process should throws Exception")
    void loanProcessShouldThrowsException() throws JsonProcessingException {
        final var execute = assertThrows(RuntimeException.class, () -> loanListener.process("{ id:\"123\"}"));
        assertEquals("Could not convert to Class", execute.getMessage());

        verify(scorePolicyUseCase,never()).execute(any(Loan.class));
        verify(agePolicyUseCase,never()).execute(any(Loan.class));
        verify(commitmentPolicyUseCase,never()).execute(any(Loan.class));
        verify(loanRepositoryGateway,never()).save(any(Loan.class));
        verify(objectMapper,atLeastOnce()).readValue(anyString(),eq(Loan.class));
    }
}