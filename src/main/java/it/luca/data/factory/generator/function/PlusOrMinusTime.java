package it.luca.data.factory.generator.function;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public abstract class PlusOrMinusTime implements DataSupplier<LocalDateTime> {

    protected final Signum signum;
    protected final TimeAmount amount;

    @Override
    public LocalDateTime apply() {

        ChronoUnit unit = amount.getChronoUnit();
        Integer quantity = amount.getQuantity();
        return signum == Signum.MINUS ?
                LocalDateTime.now().minus(quantity, unit) :
                LocalDateTime.now().plus(quantity, unit);
    }
}
