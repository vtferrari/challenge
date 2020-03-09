package br.com.vtferrari.challenge.repository.gateway;

import br.com.vtferrari.challenge.repository.LoanRepository;
import br.com.vtferrari.challenge.repository.gateway.convert.LoanDatabaseToLoanConvert;
import br.com.vtferrari.challenge.repository.gateway.convert.LoanToLoanDatabaseConvert;
import br.com.vtferrari.challenge.repository.model.LoanDatabase;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.domain.Term;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanRepositoryGatewayImplTest {
    @InjectMocks
    private LoanRepositoryGatewayImpl loanRepositoryGateway;
    @Mock
    private LoanRepository loanRepository;
    @Spy
    private LoanToLoanDatabaseConvert loanToLoanDatabaseConvert;
    @Spy
    private LoanDatabaseToLoanConvert loanDatabaseToLoanConvert;

    @Test
    @DisplayName("Get loan by id")
    void findById() {
        final var loan = Loan.builder().id("123").score(600).build();
        when(loanRepository.findById(anyString())).thenReturn(Mono.just(LoanDatabase.builder().score(600).build()));
        final var execute = loanRepositoryGateway.findById(loan).block();
        assertEquals(600, execute.getScore());

        verify(loanDatabaseToLoanConvert,atLeastOnce()).converter(any(LoanDatabase.class));
        verify(loanToLoanDatabaseConvert,never()).converter(any(Loan.class));
        verify(loanRepository,atLeastOnce()).findById(anyString());
        verify(loanRepository,never()).save(any(LoanDatabase.class));
    }

    @Test
    @DisplayName("Save new loan")
    void save() {
        final var loan = Loan.builder().id("123").terms(Term.SIX).score(600).build();
        when(loanRepository.save(any(LoanDatabase.class))).thenReturn(Mono.just(LoanDatabase.builder().score(600).build()));
        final var execute = loanRepositoryGateway.save(loan).block();
        assertEquals(600, execute.getScore());

        verify(loanDatabaseToLoanConvert,atLeastOnce()).converter(any(LoanDatabase.class));
        verify(loanToLoanDatabaseConvert,atLeastOnce()).converter(any(Loan.class));
        verify(loanRepository,never()).findById(anyString());
        verify(loanRepository,atLeastOnce()).save(any(LoanDatabase.class));
    }
}