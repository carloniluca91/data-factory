package it.luca.data.factory.generator.function;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.temporal.ChronoUnit;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeAmount {

    protected final ChronoUnit chronoUnit;
    protected final Integer quantity;

    public static TimeAmount of(ChronoUnit unit, Integer quantity) {

        return new TimeAmount(unit, quantity);
    }
}
