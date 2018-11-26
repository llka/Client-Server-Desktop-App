package ru.sportequipment.logic;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.dao.ContactDAO;
import ru.sportequipment.entity.Contact;
import ru.sportequipment.exception.DataBaseException;
import ru.sportequipment.exception.LogicException;

import java.util.List;

public class ContactLogic {
    private static final Logger logger = LogManager.getLogger(ContactLogic.class);

    private ContactDAO contactDAO;

    public ContactLogic() {
        contactDAO = new ContactDAO();
    }

    public Contact login(String email, String password) throws LogicException {
        try {
            if (contactDAO.login(email, encodePassword(password, email))) {
                return contactDAO.getByEmail(email);
            } else {
                throw new LogicException("Wrong email or password!");
            }
        } catch (DataBaseException e) {
            throw new LogicException("Cannot login!");
        }
    }

    public Contact register(Contact contact) throws LogicException {
        try {
            contactDAO.register(encodePassword(contact));
            return contactDAO.getByEmail(contact.getEmail());
        } catch (DataBaseException e) {
            throw new LogicException("Cannot register!");
        }
    }

    public List<Contact> getAll() throws LogicException {
        try {
            return contactDAO.getAll();
        } catch (DataBaseException e) {
            throw new LogicException("Cannot get all contacts!");
        }
    }

    public Contact update(Contact contact) throws LogicException {

        try {

            if (contactDAO.getByEmail(contact.getEmail()) == null) {
                throw new LogicException("You can not change email!");
            }

            if (!encodePassword(contact.getPassword(), contact.getEmail())
                    .equals(contactDAO.getByEmail(contact.getEmail()).getPassword())) {
                encodePassword(contact);
            }

            contactDAO.update(contact);
            return contactDAO.getByEmail(contact.getEmail());
        } catch (DataBaseException e) {
            throw new LogicException("Cannot update contact");
        }
    }

    public void delete(Contact contact) throws LogicException {
        try {
            contactDAO.deleteById(contact.getId());
        } catch (DataBaseException e) {
            throw new LogicException("Cannot delete contact!");
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