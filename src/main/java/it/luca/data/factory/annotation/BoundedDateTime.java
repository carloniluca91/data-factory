package it.luca.data.factory.annotation;

import it.luca.data.factory.generator.function.PlusOrMinusTime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BoundedDateTime {

    BoundedDateTime.Bound lower() default @Bound(
            signum = PlusOrMinusTime.Signum.MINUS,
            amount = 0,
            unit = ChronoUnit.SECONDS);

    BoundedDateTime.Bound upper() default @Bound(
            signum = PlusOrMinusTime.Signum.PLUS,
            amount = 0,
            unit = ChronoUnit.SECONDS);

    @interface Bound {

        PlusOrMinusTime.Signum signum();

        int amount();

        ChronoUnit unit();
    }
}
