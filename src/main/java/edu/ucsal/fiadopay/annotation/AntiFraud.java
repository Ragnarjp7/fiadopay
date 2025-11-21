package edu.ucsal.fiadopay.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AntiFraud {
    String name() default "default";
    double threshold() default 1000.0;
}
