package it.luca.data.factory.generator.function;

import it.luca.data.factory.annotation.BoundedDateTime;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static it.luca.utils.time.Supplier.now;

public class BoundedDateTimeSupplier extends DataAnnotationSupplier<BoundedDateTime, LocalDateTime> {

    public BoundedDateTimeSupplier(BoundedDateTime annotation) {
        super(annotation);
    }

    @Override
    public LocalDateTime apply() {

        BoundedDateTime.Bound lowerBound = annotation.lower();
        BoundedDateTime.Bound upperBound = annotation.upper();
        Function<BoundedDateTime.Bound, LocalDateTime> fromBound = bound -> {

            LocalDateTime localDateTime;
            switch (bound.signum()) {
                case MINUS: localDateTime = now().minus(bound.amount(), bound.unit()); break;
                case PLUS: localDateTime = now().plus(bound.amount(), bound.unit()); break;
                default: throw new NoSuchElementException(String.format("Unmatched %s: %s",
                        PlusOrMinusTime.Signum.class.getSimpleName(), bound.signum()));
            }

            return localDateTime;
        };

        Function<LocalDateTime, Long> toEpochSecond = l ->
                l.toEpochSecond(ZoneOffset.of(ZoneOffset.systemDefault().getId()));

        LocalDateTime lower = fromBound.apply(lowerBound);
        long deltaInSeconds = fromBound.andThen(toEpochSecond).apply(upperBound) - toEpochSecond.apply(lower);
        return lower.plus((long) (Math.random() * deltaInSeconds), ChronoUnit.SECONDS);
    }
}
