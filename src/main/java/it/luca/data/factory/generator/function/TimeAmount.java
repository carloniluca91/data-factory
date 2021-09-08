package it.luca.data.factory.generator.function;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.temporal.ChronoUnit;

@Getter
@AllArgsConstructor
public abstract class TimeAmount {

    protected final ChronoUnit chronoUnit;
    protected final Integer quantity;
}
