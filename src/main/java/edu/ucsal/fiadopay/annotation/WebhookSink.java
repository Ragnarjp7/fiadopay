package edu.ucsal.fiadopay.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebhookSink {
    String name() default "defaultSink";
}
