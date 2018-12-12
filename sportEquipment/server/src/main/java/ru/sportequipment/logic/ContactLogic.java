package ru.sportequipment.logic;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.dao.ContactDAO;
import ru.sportequipment.dao.SkatesDAO;
import ru.sportequipment.dao.StickDAO;
import ru.sportequipment.entity.Contact;
import ru.sportequipment.entity.Equipment;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.entity.enums.RoleEnum;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.exception.DataBaseException;

import java.util.ArrayList;
import java.util.List;

public class ContactLogic {
    private static final Logger logger = LogManager.getLogger(ContactLogic.class);

    private ContactDAO contactDAO;
    private StickDAO stickDAO;
    private SkatesDAO skatesDAO;

    public ContactLogic() {
        contactDAO = new ContactDAO();
        stickDAO = new StickDAO();
        skatesDAO = new SkatesDAO();
    }

    public Contact login(String email, String password) throws ApplicationException {
        if (contactDAO.login(email, password)) {
            return fetchContactsEquipment(contactDAO.getByEmail(email));
        } else {
            throw new ApplicationException("Wrong email or password!", ResponseStatus.BAD_REQUEST);
        }
    }

    public Contact register(Contact contact) throws ApplicationException {
        contact.setRole(RoleEnum.USER);
        contactDAO.register(contact);
        return fetchContactsEquipment(contactDAO.getByEmail(contact.getEmail()));
    }

    public Contact getById(int contactId) throws ApplicationException {
        return fetchContactsEquipment(contactDAO.getById(contactId));
    }

    public Contact getByEmail(String email) throws ApplicationException {
        return fetchContactsEquipment((contactDAO.getByEmail(email)));
    }

    public List<Contact> getAll() throws ApplicationException {
        List<Contact> contacts = contactDAO.getAll();
        for (Contact contact : contacts) {
            fetchContactsEquipment(contact);
        }
        return contacts;
    }

    public Contact update(Contact contact) throws ApplicationException {
        try {
            contactDAO.getByEmail(contact.getEmail());
        } catch (DataBaseException e) {
            throw new ApplicationException("You can not change email!", ResponseStatus.BAD_REQUEST);
        }

        contactDAO.update(contact);
        return fetchContactsEquipment(contactDAO.getByEmail(contact.getEmail()));
    }

    public void delete(Contact contact) throws ApplicationException {
        contactDAO.deleteById(contact.getId());
    }

    public void delete(String contactIdString, String email) throws ApplicationException {
        int id = -1;
        if (contactIdString != null && !contactIdString.isEmpty()) {
            id = Integer.parseInt(contactIdString);
            contactDAO.getById(id);
            contactDAO.deleteById(id);
        } else if (email != null && !email.isEmpty()) {
            Contact contact = contactDAO.getByEmail(email);
            contactDAO.deleteById(contact.getId());
        } else {
            throw new ApplicationException("No contact id or email!", ResponseStatus.BAD_REQUEST);
        }
    }

    private Contact fetchContactsEquipment(Contact contact) throws ApplicationException {
        if (contact != null) {
            List<Equipment> equipment = new ArrayList<>();
            equipment.addAll(skatesDAO.getContactsSkates(contact.getId()));
            equipment.addAll(stickDAO.getContactsSticks(contact.getId()));

            contact.setBookedEquipment(equipment);
            return contact;
        } else {
            throw new ApplicationException("Contact is null!", ResponseStatus.BAD_REQUEST);
        }
    }
}
