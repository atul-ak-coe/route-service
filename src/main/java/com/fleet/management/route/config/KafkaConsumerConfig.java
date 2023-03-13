package com.fleet.management.route.config;

import com.fleet.management.route.model.RouteDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ReceiverOptions<String, RouteDetail> kafkaReceiverOptions(@Value(value = "${fleet.route.router-topic}") String topic, KafkaProperties kafkaProperties) {
        ReceiverOptions<String, RouteDetail> basicReceiverOptions = ReceiverOptions.create(kafkaProperties.buildConsumerProperties());
        return basicReceiverOptions.subscription(Collections.singletonList(topic));
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, RouteDetail> reactiveKafkaConsumerTemplate(ReceiverOptions<String, RouteDetail> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<String, RouteDetail>(kafkaReceiverOptions);
    }
}
