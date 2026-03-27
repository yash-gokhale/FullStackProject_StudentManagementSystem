package org.student.studentservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.student.constants.KafkaConstants;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic studentPaymentTopic() {
        return TopicBuilder.name(KafkaConstants.PAYMENT_NOTIFY)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic studentElectionTopic() {
        return TopicBuilder.name(KafkaConstants.ELECTION_SIGNAL)
                .partitions(3)
                .replicas(1)
                .build();
    }
}