package domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Price implements EntityInterface {
    private UUID uuid;
    private Date date;
    private Float price;

    public Price() {
    }

    public Price(Date date, Float price) {
        this.date = date;
        this.price = price;
    }

    public Price(UUID uuid, Date date, Float price) {
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

