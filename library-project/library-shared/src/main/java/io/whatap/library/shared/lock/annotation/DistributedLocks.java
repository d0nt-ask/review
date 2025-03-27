package io.whatap.library.shared.lock.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLocks {
    String lockName();

    String keys();

    long waitTime() default 30;

    long leaseTime() default 10;
}
