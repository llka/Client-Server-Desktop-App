package ru.sportequipment.entity;

import ru.sportequipment.entity.enums.StickType;

import java.util.Objects;

public class Stick extends Equipment implements DatabaseEntity {
    private int id;
    private StickType stickType;

    public Stick() {
    }

    public StickType getStickType() {
        return stickType;
    }

    public void setStickType(StickType stickType) {
        this.stickType = stickType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stick stick = (Stick) o;
        return id == stick.id &&
                stickType == stick.stickType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stickType);
    }
}
