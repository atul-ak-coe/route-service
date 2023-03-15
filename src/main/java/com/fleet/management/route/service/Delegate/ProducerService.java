package com.fleet.management.route.service.Delegate;

import com.fleet.management.route.model.RouteDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import javax.validation.constraints.NotNull;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {

    @Value(value = "${fleet.route.producer.topic.notification-topic}")
    private String topic;

    @Value(value = "${fleet.route.producer.retry-count}")
    private long maxRetryCount;

    @NotNull
    private final ReactiveKafkaProducerTemplate<String, RouteDetail> kafkaProducerTemplate;

    public void send(RouteDetail routeDetail) {
        log.info("send to topic={}, {}={},", topic, Object.class.getSimpleName(), routeDetail);
        kafkaProducerTemplate.send(topic, routeDetail)
                .doOnSuccess(senderResult -> log.info("sent {} offset : {}", routeDetail, senderResult.recordMetadata().offset()))
                .retryWhen(Retry.backoff(maxRetryCount, Duration.ofMillis(200)).transientErrors(true))
                .onErrorResume(err -> {
                    log.info("Retries exhausted for " + routeDetail);
                    return Mono.empty();
                })
                .subscribe();
    }
}
