package ru.sportequipment.logic;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.dao.SkatesDAO;
import ru.sportequipment.entity.Contact;
import ru.sportequipment.entity.Skates;
import ru.sportequipment.exception.DataBaseException;

import java.util.List;

public class SkatesLogic {
    private static final Logger logger = LogManager.getLogger(SkatesLogic.class);


    private SkatesDAO skatesDAO;

    public SkatesLogic() {
        skatesDAO = new SkatesDAO();
    }

    public void save(Skates skates) throws DataBaseException {
        skatesDAO.save(skates);
    }

    public Skates update(Skates skates) throws DataBaseException {
        return skatesDAO.update(skates);
    }

    public Skates getById(int id) throws DataBaseException {
        return skatesDAO.getById(id);
    }

    public List<Skates> getAll() throws DataBaseException {
        return skatesDAO.getAll();
    }

    public List<Skates> getContactsSkates(int contactId) throws DataBaseException {
        return skatesDAO.getContactsSkates(contactId);
    }

    public void delete(Skates skates) throws DataBaseException {
        skatesDAO.delete(skates);
    }

    public void bookSkates(Contact contact, Skates skates) throws DataBaseException {
        skatesDAO.bookSkates(contact, skates);
    }
}
