package ru.sportequipment.dto;

import ru.sportequipment.entity.Skates;

import java.util.List;

public class SkatesListDTO {
    private List<Skates> skatesList;

    public SkatesListDTO() {
    }

    public SkatesListDTO(List<Skates> skatesList) {
        this.skatesList = skatesList;
    }

    public List<Skates> getSkatesList() {
        return skatesList;
    }

    public void setSkatesList(List<Skates> skatesList) {
        this.skatesList = skatesList;
    }

    @Override
    public String toString() {
        return "SkatesListDTO{" +
                "skatesList=" + skatesList +
                '}';
    }
}
