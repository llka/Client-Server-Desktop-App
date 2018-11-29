package ru.sportequipment.logic;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.dao.ContactDAO;
import ru.sportequipment.dao.SkatesDAO;
import ru.sportequipment.dao.StickDAO;
import ru.sportequipment.entity.Contact;
import ru.sportequipment.entity.Equipment;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;

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
        if (contactDAO.login(email, encodePassword(password, email))) {
            return fetchContactsEquipment(contactDAO.getByEmail(email));
        } else {
            throw new ApplicationException("Wrong email or password!", ResponseStatus.BAD_REQUEST);
        }
    }

    public Contact register(Contact contact) throws ApplicationException {
        contactDAO.register(encodePassword(contact));
        return fetchContactsEquipment(contactDAO.getByEmail(contact.getEmail()));
    }

    public Contact getById(int contactId) throws ApplicationException {
        return fetchContactsEquipment(contactDAO.getById(contactId));
    }

    public List<Contact> getAll() throws ApplicationException {
        List<Contact> contacts = contactDAO.getAll();
        for (Contact contact : contacts) {
            fetchContactsEquipment(contact);
        }
        return contacts;
    }

    public Contact update(Contact contact) throws ApplicationException {
        if (contactDAO.getByEmail(contact.getEmail()) == null) {
            throw new ApplicationException("You can not change email!", ResponseStatus.BAD_REQUEST);
        }

        if (!encodePassword(contact.getPassword(), contact.getEmail())
                .equals(contactDAO.getByEmail(contact.getEmail()).getPassword())) {
            encodePassword(contact);
        }

        contactDAO.update(contact);
        return fetchContactsEquipment(contactDAO.getByEmail(contact.getEmail()));
    }

    public void delete(Contact contact) throws ApplicationException {
        contactDAO.deleteById(contact.getId());
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

    private String encodePassword(String password, String email) {
        logger.debug("decoded password: " + password);
        password = encodeWithSHA512(password, email);
        logger.debug("encoded password: " + password);
        return password;
    }

    private Contact encodePassword(Contact contact) {
        logger.debug("decoded password: " + contact.getPassword());
        contact.setPassword(encodeWithSHA512(contact.getPassword(), contact.getEmail()));
        logger.debug("encoded password: " + contact.getPassword());
        return contact;
    }


    private String encodeWithSHA512(String data, String salt) {
        return DigestUtils.sha512Hex(data + salt);
    }


}
