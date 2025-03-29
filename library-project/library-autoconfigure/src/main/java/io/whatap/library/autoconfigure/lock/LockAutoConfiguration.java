package io.whatap.library.autoconfigure.lock;

import io.whatap.library.shared.lock.aspect.DistributedLockAspect;
import io.whatap.library.shared.lock.aspect.ScheduleLockAspect;
import io.whatap.library.shared.lock.excecutor.TransactionExecutor;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = {RedissonAutoConfigurationV2.class})
public class LockAutoConfiguration {


    @Bean
    public TransactionExecutor transactionExecutor() {
        return new TransactionExecutor();
    }

    @Bean
    public DistributedLockAspect distributedLockAspect(TransactionExecutor transactionExecutor, RedissonClient redissonClient) {
        return new DistributedLockAspect(transactionExecutor, redissonClient);
    }

    @Bean
    public ScheduleLockAspect scheduleLockAspect(RedissonClient redissonClient) {
        return new ScheduleLockAspect(redissonClient);
    }
}
