package it.luca.data.factory.annotation;

import it.luca.data.factory.generator.function.DataSupplier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDateTime;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RandomDateTime {

    Class<? extends DataSupplier<LocalDateTime>> supplier();
}
