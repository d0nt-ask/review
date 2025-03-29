package io.whatap.library.shared.lock.aspect;

import io.whatap.library.shared.lock.annotation.DistributedLock;
import io.whatap.library.shared.lock.annotation.DistributedLocks;
import io.whatap.library.shared.lock.excecutor.TransactionExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Aspect
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DistributedLockAspect {

    private final TransactionExecutor transactionExecutor;
    private final RedissonClient redissonClient;

    @Pointcut("@annotation(io.whatap.library.shared.lock.annotation.DistributedLock)")
    public void distributedLock() {
    }

    @Pointcut("@annotation(io.whatap.library.shared.lock.annotation.DistributedLocks)")
    public void distributedLocks() {
    }

    @Around("distributedLock()")
    public Object aroundDistributedLock(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();

        SpelExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        DistributedLock distributedLock = methodSignature.getMethod().getAnnotation(DistributedLock.class);

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], pjp.getArgs()[i]);
        }
        Object value = parser.parseExpression(distributedLock.key()).getValue(context);
        RLock lock = redissonClient.getFairLock(distributedLock.lockName() + ":" + value.toString());
        try {
            if (lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), TimeUnit.SECONDS)) {
                return transactionExecutor.runInTransaction(pjp);
            } else {
                throw new RuntimeException("잠시후 시도해 주세요.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Around("distributedLocks()")
    public Object aroundDistributedLocks(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();

        SpelExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        DistributedLocks distributedLocks = methodSignature.getMethod().getAnnotation(DistributedLocks.class);

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], pjp.getArgs()[i]);
        }
        Collection value = parser.parseExpression(distributedLocks.keys()).getValue(context, Collection.class);
        RLock[] rLocks = new RLock[value.size()];
        for (int i = 0; i < value.size(); i++) {
            RLock lock = redissonClient.getLock(distributedLocks.lockName() + ":" + value.toArray()[i].toString());
            rLocks[i] = lock;
        }
        RLock multiLock = redissonClient.getMultiLock(rLocks);

        try {

            if (multiLock.tryLock(distributedLocks.waitTime(), distributedLocks.leaseTime(), TimeUnit.SECONDS)) {
                return transactionExecutor.runInTransaction(pjp);
            } else {
                throw new RuntimeException("잠시후 시도해 주세요.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (multiLock.isHeldByCurrentThread()) {
                multiLock.unlock();
            }
        }
    }
}
