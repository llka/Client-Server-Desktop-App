package ru.sportequipment.dto;

import ru.sportequipment.entity.Contact;

import java.util.List;

public class ContactsDTO {
    private List<Contact> contacts;

    public ContactsDTO() {
    }

    public ContactsDTO(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "ContactsDTO{" +
                "contacts=" + contacts +
                '}';
    }
}
