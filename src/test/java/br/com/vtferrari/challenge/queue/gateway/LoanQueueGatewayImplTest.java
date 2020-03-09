package br.com.vtferrari.challenge.queue.gateway;

import br.com.vtferrari.challenge.usecase.domain.Loan;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.MonoToListenableFutureAdapter;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LoanQueueGatewayImplTest {
    @InjectMocks
    private LoanQueueGatewayImpl loanQueueGateway;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @Spy
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Send loan to queueo")
    void sendLoanToQueue() throws JsonProcessingException {
        final var loan = Loan.builder().id("123").score(600).build();
        when(kafkaTemplate.send(anyString(), anyString(), anyString())).thenReturn(new MonoToListenableFutureAdapter<>(Mono.empty()));
        final var execute = loanQueueGateway.send(loan).block();
        assertEquals(600, execute.getScore());
        verify(kafkaTemplate,atLeastOnce()).send(anyString(), anyString(), anyString());
        verify(objectMapper,atLeastOnce()).writeValueAsString(any());
    }

    @Test
    @DisplayName("Send loan to queue should throws Exception")
    void sendLoanToQueueShouldThrowsException() throws Exception {
        final var loan = Loan.builder().id("123").score(600).build();
        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("") {
        });
        final var execute = assertThrows(RuntimeException.class, () -> loanQueueGateway.send(loan).block());
        assertEquals("Error to convert to json", execute.getMessage());

        verify(kafkaTemplate,never()).send(anyString(), anyString(), anyString());
        verify(objectMapper,atLeastOnce()).writeValueAsString(any());

    }

}