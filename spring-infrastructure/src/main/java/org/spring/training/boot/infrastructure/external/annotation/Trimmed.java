package org.spring.training.boot.infrastructure.external.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Trimmed {

    boolean arguments() default true;

    boolean returnValue() default false;

}
