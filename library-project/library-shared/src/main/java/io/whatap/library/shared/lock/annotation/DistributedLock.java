package io.whatap.library.shared.lock.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    String lockName();

    String key();

    long leaseTime() default 10;

    long waitTime() default 30;
}
