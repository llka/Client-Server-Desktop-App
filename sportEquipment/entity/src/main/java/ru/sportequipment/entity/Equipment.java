package ru.sportequipment.entity;

import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.util.Date;

public abstract class Equipment {
    private BigDecimal costPerHour;
    private Date bookedFrom;
    private Date bookedTo;

    public BigDecimal getCostPerHour() {
        return costPerHour;
    }

    public void setCostPerHour(BigDecimal costPerHour) {
        this.costPerHour = costPerHour;
    }

    public Date getBookedFrom() {
        return bookedFrom;
    }

    public void setBookedFrom(Date bookedFrom) {
        this.bookedFrom = bookedFrom;
    }

    public Date getBookedTo() {
        return bookedTo;
    }

    public void setBookedTo(Date bookedTo) {
        this.bookedTo = bookedTo;
    }

    public void bookForHours(int hours) {
        bookedFrom = new Date();
        bookedTo = DateUtils.addHours(bookedFrom, hours);
    }
}
