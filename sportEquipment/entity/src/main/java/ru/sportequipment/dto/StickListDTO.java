package ru.sportequipment.dto;

import ru.sportequipment.entity.Stick;

import java.util.List;

public class StickListDTO {
    private List<Stick> stickList;

    public StickListDTO() {
    }

    public StickListDTO(List<Stick> stickList) {
        this.stickList = stickList;
    }

    public List<Stick> getStickList() {
        return stickList;
    }

    public void setStickList(List<Stick> stickList) {
        this.stickList = stickList;
    }

    @Override
    public String toString() {
        return "StickListDTO{" +
                "stickList=" + stickList +
                '}';
    }
}
