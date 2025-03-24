package io.whatap.library.autoconfigure.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@AutoConfiguration
@Slf4j
public class KafkaConsumerAutoConfiguration {

    @Bean
    @ConditionalOnBean(KafkaTemplate.class)
    public DefaultErrorHandler deadLetterPublishingRecover(KafkaTemplate<?, ?> template) {
        DeadLetterPublishingRecoverer recover = new DeadLetterPublishingRecoverer(template,
                (r, e) -> {
                    log.error(e.getMessage(), e);
                    return new TopicPartition(r.topic() + "-dlt", r.partition());
                });
        return new DefaultErrorHandler(recover, new FixedBackOff(0L, 0L));
    }
}
