package domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Price implements EntityInterface {
    private UUID uuid;
    private Date date;
    private BigDecimal price;

    public Price() {
    }

    public Price(Date date, BigDecimal price) {
        this.date = date;
        this.price = price;
    }

    public Price(UUID uuid, Date date, BigDecimal price) {
        this.uuid = uuid;
        this.date = date;
        this.price = price;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return "Price: " +
                " Date = " + date +
                ", Price = " + price;
    }
}

