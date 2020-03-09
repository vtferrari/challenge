package br.com.vtferrari.challenge.queue;


import br.com.vtferrari.challenge.queue.gateway.LoanQueueGatewayImpl;
import br.com.vtferrari.challenge.usecase.AgePolicyUseCase;
import br.com.vtferrari.challenge.usecase.CommitmentPolicyUseCase;
import br.com.vtferrari.challenge.usecase.ScorePolicyUseCase;
import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.domain.Status;
import br.com.vtferrari.challenge.usecase.gateway.LoanRepositoryGateway;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class LoanListener {

    private final ScorePolicyUseCase scorePolicyUseCase;
    private final AgePolicyUseCase agePolicyUseCase;
    private final CommitmentPolicyUseCase commitmentPolicyUseCase;
    private final LoanRepositoryGateway loanRepositoryGateway;
    private final ObjectMapper objectMapper;

    @KafkaListener(groupId = "loan", topics = LoanQueueGatewayImpl.CHALLENGE_LOAN_PROCESS)
    public void process(@Payload String message) {
        Mono.just(message)
                .map(this::getLoan)
                .flatMap(agePolicyUseCase::execute)
                .flatMap(scorePolicyUseCase::execute)
                .flatMap(commitmentPolicyUseCase::execute)
                .map(loan -> loan.toBuilder().status(Status.COMPLETED).build())
                .flatMap(loanRepositoryGateway::save)
                .block();
    }


    private Loan getLoan(@Payload String message) {
        try {
            return objectMapper.readValue(message, Loan.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not convert to Class");
        }
    }
}
