package ru.sportequipment.entity;

import ru.sportequipment.entity.enums.RoleEnum;

public class Visitor {
    private RoleEnum role;
    private Contact contact;

    public Visitor() {
        role = RoleEnum.GUEST;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
