package io.whatap.library.shared.lock.aspect;

import io.whatap.library.shared.lock.annotation.ScheduleLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

@Aspect
@RequiredArgsConstructor
@Slf4j
public class ScheduleLockAspect {
    private final RedissonClient redissonClient;

    @Pointcut("@annotation(io.whatap.library.shared.lock.annotation.ScheduleLock)")
    public void scheduleLock() {
    }

    @Around("scheduleLock()")
    public Object aroundScheduleLock(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        ScheduleLock scheduleLock = signature.getMethod().getAnnotation(ScheduleLock.class);
        RLock lock = redissonClient.getLock(scheduleLock.lockName());
        try {
            if (lock.tryLock(0, scheduleLock.leaseTime(), TimeUnit.SECONDS)) {
                return pjp.proceed();
            }
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return null;
    }

}
