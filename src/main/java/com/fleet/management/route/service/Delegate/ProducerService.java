package com.fleet.management.route.service.Delegate;

import com.fleet.management.route.model.RouteDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerService {

    @Value(value = "${fleet.route.notification-topic}")
    private String topic;

    @NotNull
    private final ReactiveKafkaProducerTemplate<String, RouteDetail> kafkaProducerTemplate;

    public void send(RouteDetail routeDetail) {
        log.info("send to topic={}, {}={},", topic, Object.class.getSimpleName(), routeDetail);
        kafkaProducerTemplate.send(topic, routeDetail)
                .doOnSuccess(senderResult -> log.info("sent {} offset : {}", routeDetail, senderResult.recordMetadata().offset()))
                .subscribe();
    }
}
