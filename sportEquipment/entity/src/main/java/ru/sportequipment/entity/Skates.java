package ru.sportequipment.entity;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ru.sportequipment.entity.enums.SkatesType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.Objects;

@JsonTypeName("skates")
public class Skates extends Equipment implements DatabaseEntity {
    private int id;
    private SkatesType type;
    @Positive
    @Max(50)
    @Min(35)
    private int size;

    public Skates() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SkatesType getType() {
        return type;
    }

    public void setType(SkatesType type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skates skates = (Skates) o;
        return id == skates.id &&
                size == skates.size &&
                type == skates.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, size);
    }

    @Override
    public String toString() {
        return "Skates{" +
                "id=" + id +
                ", type=" + type +
                ", size=" + size +
                '}';
    }
}
