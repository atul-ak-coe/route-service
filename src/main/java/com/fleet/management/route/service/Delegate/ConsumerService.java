package com.fleet.management.route.service.Delegate;

import com.fleet.management.route.exception.ReceiverRecordException;
import com.fleet.management.route.model.RouteDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import javax.validation.constraints.NotNull;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerService implements CommandLineRunner {

    @NotNull
    private final ReactiveKafkaConsumerTemplate<String, RouteDetail> reactiveKafkaConsumerTemplate;

    @Value(value = "${fleet.topic.consumer.retry-count}")
    private long maxRetryCount;

    private Flux<RouteDetail> consumer() {
        return reactiveKafkaConsumerTemplate
                .receive()
                // .delayElements(Duration.ofSeconds(2L)) // BACKPRESSURE
                .doOnNext(consumerRecord -> {
                    log.info("received key={}, value={} from topic={}, offset={}",
                            consumerRecord.key(),
                            consumerRecord.value(),
                            consumerRecord.topic(),
                            consumerRecord.offset());
                    if (consumerRecord.value().equals("fail")) {
                        throw new ReceiverRecordException(consumerRecord, new RuntimeException("Failed to consume message."));
                    }
                    consumerRecord.receiverOffset().acknowledge();
                })
                .retryWhen(Retry.backoff(maxRetryCount, Duration.ofMillis(500)).transientErrors(true))
                .map(ConsumerRecord::value)
                .doOnNext(routeDetail -> log.info("successfully consumed {}={}", RouteDetail.class.getSimpleName(), routeDetail))
                .onErrorContinue((err, record) -> {
                    ReceiverRecordException ex = (ReceiverRecordException) err.getCause();
                    log.error("Retries exhausted for " + ex.getReceiverRecord().value());
                    ex.getReceiverRecord().receiverOffset().acknowledge();
                })
                .doOnError(throwable -> log.error("something bad happened while consuming : {}", throwable.getMessage()));
    }

    @Override
    public void run(String... args) throws Exception {
        consumer().subscribe();
    }
}
