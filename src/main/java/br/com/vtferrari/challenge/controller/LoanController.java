package br.com.vtferrari.challenge.controller;

import br.com.vtferrari.challenge.controller.convert.LoanIdToLoanConvert;
import br.com.vtferrari.challenge.controller.convert.LoanRequestResourceToLoanConvert;
import br.com.vtferrari.challenge.controller.convert.LoanToLoanResponseResourceConvert;
import br.com.vtferrari.challenge.controller.convert.LoanToLoanTokenResponseResourceConvert;
import br.com.vtferrari.challenge.controller.resource.LoanRequestResource;
import br.com.vtferrari.challenge.controller.resource.LoanResponseResource;
import br.com.vtferrari.challenge.controller.resource.LoanTokenResponseResource;
import br.com.vtferrari.challenge.usecase.CheckLoanUseCase;
import br.com.vtferrari.challenge.usecase.CreateLoanAndSendToQueueUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/v1/loan")
public class LoanController {
    private final CreateLoanAndSendToQueueUseCase createLoanAndSendToQueueUseCase;
    private final CheckLoanUseCase checkLoanUseCase;
    private final LoanRequestResourceToLoanConvert loanRequestResourceToLoanConvert;
    private final LoanIdToLoanConvert loanIdToLoanConvert;
    private final LoanToLoanTokenResponseResourceConvert loanToLoanTokenResponseResourceConvert;
    private final LoanToLoanResponseResourceConvert loanToLoanResponseResourceConvert;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<LoanTokenResponseResource> loan(@RequestBody @Valid final LoanRequestResource loanResponseResource) {
        return Mono.just(loanResponseResource)
                .map(loanRequestResourceToLoanConvert::converter)
                .flatMap(createLoanAndSendToQueueUseCase::execute)
                .map(loanToLoanTokenResponseResourceConvert::converter);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<LoanResponseResource> loan(@PathVariable String id) {
        return Mono.just(id)
                .map(loanIdToLoanConvert::converter)
                .flatMap(checkLoanUseCase::execute)
                .map(loanToLoanResponseResourceConvert::converter);
    }
}
