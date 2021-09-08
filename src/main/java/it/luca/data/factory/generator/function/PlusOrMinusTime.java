package it.luca.data.factory.generator.function;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@AllArgsConstructor
public abstract class PlusOrMinusTime implements DataSupplier<LocalDateTime> {

    protected final Signum signum;
    protected final ChronoUnit chronoUnit;
    protected final Integer quantity;

    public PlusOrMinusTime(Signum signum, TimeAmount amount) {
        this(signum, amount.getChronoUnit(), amount.getQuantity());
    }

    @Override
    public LocalDateTime apply() {

        return signum == Signum.MINUS ?
                LocalDateTime.now().minus(quantity, chronoUnit) :
                LocalDateTime.now().plus(quantity, chronoUnit);
    }
}
