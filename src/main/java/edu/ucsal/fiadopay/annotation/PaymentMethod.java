package edu.ucsal.fiadopay.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PaymentMethod {
    String type() default "CARD";
}
