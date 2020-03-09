package br.com.vtferrari.challenge.queue.gateway;

import br.com.vtferrari.challenge.usecase.domain.Loan;
import br.com.vtferrari.challenge.usecase.gateway.LoanQueueGateway;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class LoanQueueGatewayImpl implements LoanQueueGateway {
    public static final String CHALLENGE_LOAN_PROCESS = "challenge.loan.process";
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Loan> send(Loan loan) {
        final var send = kafkaTemplate.send(CHALLENGE_LOAN_PROCESS, loan.getId(), getLoanAsStringJson(loan));
        return Mono.just(send)
                .then(Mono.just(loan));
    }

    private String getLoanAsStringJson(Loan loan) {
        try {
            return objectMapper.writeValueAsString(loan);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error to convert to json");
        }
    }
}
