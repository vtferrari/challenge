package br.com.vtferrari.challenge.controller;

import br.com.vtferrari.challenge.controller.convert.LoanIdToLoanConvert;
import br.com.vtferrari.challenge.controller.convert.LoanRequestResourceToLoanConvert;
import br.com.vtferrari.challenge.controller.convert.LoanToLoanResponseResourceConvert;
import br.com.vtferrari.challenge.controller.convert.LoanToLoanTokenResponseResourceConvert;
import br.com.vtferrari.challenge.controller.resource.LoanRequestResource;
import br.com.vtferrari.challenge.repository.model.LoanDatabase;
import br.com.vtferrari.challenge.usecase.CheckLoanUseCase;
import br.com.vtferrari.challenge.usecase.CreateLoanAndSendToQueueUseCase;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.domain.Status;
import br.com.vtferrari.challenge.usecase.domain.Term;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanControllerTest {
    @InjectMocks
    private LoanController loanController;
    @Mock
    private CreateLoanAndSendToQueueUseCase createLoanAndSendToQueueUseCase;
    @Mock
    private CheckLoanUseCase checkLoanUseCase;
    @Spy
    private LoanRequestResourceToLoanConvert loanRequestResourceToLoanConvert;
    @Spy
    private LoanIdToLoanConvert loanIdToLoanConvert;
    @Spy
    private LoanToLoanTokenResponseResourceConvert loanToLoanTokenResponseResourceConvert;
    @Spy
    private LoanToLoanResponseResourceConvert loanToLoanResponseResourceConvert;

    @Test
    @DisplayName("Create new loan request")
    void createNewLoanRequest() {
        final var loan = Loan
                .builder()
                .id("123")
                .scorePolicy(true)
                .agePolicy(true)
                .commitmentPolicy(false)
                .status(Status.COMPLETED)
                .commitment(BigDecimal.ONE).build();
        when(createLoanAndSendToQueueUseCase.execute(any(Loan.class))).thenReturn(Mono.just(loan));
        final var execute = loanController.loan(LoanRequestResource.builder().build()).block();
        assertEquals("123", execute.getId());

        verify(createLoanAndSendToQueueUseCase,atLeastOnce()).execute(any(Loan.class)) ;
        verify(checkLoanUseCase,never()).execute(any(Loan.class)) ;
        verify(loanRequestResourceToLoanConvert,atLeastOnce()).converter(any(LoanRequestResource.class)) ;
        verify(loanIdToLoanConvert,never()).converter(anyString()) ;
        verify(loanToLoanTokenResponseResourceConvert,atLeastOnce()).converter(any(Loan.class)) ;
        verify(loanToLoanResponseResourceConvert,never()).converter(any(Loan.class)) ;
    }

    @Test
    @DisplayName("Get in processing loan")
    void getInProcessingLoan() {
        final var loan = Loan
                .builder()
                .id("123")
                .status(Status.PROCESSING)
                .commitment(BigDecimal.ONE).build();
        when(checkLoanUseCase.execute(any(Loan.class))).thenReturn(Mono.just(loan));
        final var execute = loanController.loan("123").block();
        assertEquals("SCORE", execute.getRefusedPolicy());
        assertNull(execute.getResult());


        verify(createLoanAndSendToQueueUseCase,never()).execute(any(Loan.class)) ;
        verify(checkLoanUseCase,atLeastOnce()).execute(any(Loan.class)) ;
        verify(loanRequestResourceToLoanConvert,never()).converter(any(LoanRequestResource.class)) ;
        verify(loanIdToLoanConvert,atLeastOnce()).converter(anyString()) ;
        verify(loanToLoanTokenResponseResourceConvert,never()).converter(any(Loan.class)) ;
        verify(loanToLoanResponseResourceConvert,atLeastOnce()).converter(any(Loan.class)) ;
    }


    @Test
    @DisplayName("Get loan refused by score")
    void getLoanRefusedByScore() {
        final var loan = Loan
                .builder()
                .id("123")
                .scorePolicy(false)
                .agePolicy(true)
                .commitmentPolicy(true)
                .status(Status.COMPLETED)
                .commitment(BigDecimal.ONE).build();
        when(checkLoanUseCase.execute(any(Loan.class))).thenReturn(Mono.just(loan));
        final var execute = loanController.loan("123").block();
        assertEquals("SCORE", execute.getRefusedPolicy());
        assertEquals("refused", execute.getResult());

        verify(createLoanAndSendToQueueUseCase,never()).execute(any(Loan.class)) ;
        verify(checkLoanUseCase,atLeastOnce()).execute(any(Loan.class)) ;
        verify(loanRequestResourceToLoanConvert,never()).converter(any(LoanRequestResource.class)) ;
        verify(loanIdToLoanConvert,atLeastOnce()).converter(anyString()) ;
        verify(loanToLoanTokenResponseResourceConvert,never()).converter(any(Loan.class)) ;
        verify(loanToLoanResponseResourceConvert,atLeastOnce()).converter(any(Loan.class)) ;
    }
    @Test
    @DisplayName("Get loan refused by commitment")
    void getLoanRefusedByCommitment() {
        final var loan = Loan
                .builder()
                .id("123")
                .scorePolicy(true)
                .agePolicy(true)
                .commitmentPolicy(false)
                .status(Status.COMPLETED)
                .commitment(BigDecimal.ONE).build();
        when(checkLoanUseCase.execute(any(Loan.class))).thenReturn(Mono.just(loan));
        final var execute = loanController.loan("123").block();
        assertEquals("COMMITMENT", execute.getRefusedPolicy());
        assertEquals("refused", execute.getResult());

        verify(createLoanAndSendToQueueUseCase,never()).execute(any(Loan.class)) ;
        verify(checkLoanUseCase,atLeastOnce()).execute(any(Loan.class)) ;
        verify(loanRequestResourceToLoanConvert,never()).converter(any(LoanRequestResource.class)) ;
        verify(loanIdToLoanConvert,atLeastOnce()).converter(anyString()) ;
        verify(loanToLoanTokenResponseResourceConvert,never()).converter(any(Loan.class)) ;
        verify(loanToLoanResponseResourceConvert,atLeastOnce()).converter(any(Loan.class)) ;
    }


    @Test
    @DisplayName("Get loan refused by age")
    void getLoanRefusedByAge() {
        final var loan = Loan
                .builder()
                .id("123")
                .commitmentPolicy(true)
                .scorePolicy(true)
                .agePolicy(false)
                .status(Status.COMPLETED)
                .commitment(BigDecimal.ONE)
                .build();
        when(checkLoanUseCase.execute(any(Loan.class))).thenReturn(Mono.just(loan));
        final var execute = loanController.loan("123").block();
        assertEquals("AGE", execute.getRefusedPolicy());
        assertEquals("refused", execute.getResult());

        verify(createLoanAndSendToQueueUseCase,never()).execute(any(Loan.class)) ;
        verify(checkLoanUseCase,atLeastOnce()).execute(any(Loan.class)) ;
        verify(loanRequestResourceToLoanConvert,never()).converter(any(LoanRequestResource.class)) ;
        verify(loanIdToLoanConvert,atLeastOnce()).converter(anyString()) ;
        verify(loanToLoanTokenResponseResourceConvert,never()).converter(any(Loan.class)) ;
        verify(loanToLoanResponseResourceConvert,atLeastOnce()).converter(any(Loan.class)) ;
    }

    @Test
    @DisplayName("Get loan refused by approved")
    void getLoanRefusedByApproved() {
        final var loan = Loan.builder().id("123")
                .commitmentPolicy(true)
                .scorePolicy(true)
                .agePolicy(true)
                .commitment(BigDecimal.ONE)
                .terms(Term.SIX)
                .status(Status.COMPLETED)
                .build();
        when(checkLoanUseCase.execute(any(Loan.class))).thenReturn(Mono.just(loan));
        final var execute = loanController.loan("123").block();
        assertNull(execute.getRefusedPolicy());
        assertEquals("approved", execute.getResult());

        verify(createLoanAndSendToQueueUseCase,never()).execute(any(Loan.class)) ;
        verify(checkLoanUseCase,atLeastOnce()).execute(any(Loan.class)) ;
        verify(loanRequestResourceToLoanConvert,never()).converter(any(LoanRequestResource.class)) ;
        verify(loanIdToLoanConvert,atLeastOnce()).converter(anyString()) ;
        verify(loanToLoanTokenResponseResourceConvert,never()).converter(any(Loan.class)) ;
        verify(loanToLoanResponseResourceConvert,atLeastOnce()).converter(any(Loan.class)) ;
    }
}