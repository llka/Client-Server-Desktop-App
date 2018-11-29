package ru.sportequipment.logic;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.dao.StickDAO;
import ru.sportequipment.entity.Stick;
import ru.sportequipment.exception.DataBaseException;

import java.util.List;

public class StickLogic {
    private static final Logger logger = LogManager.getLogger(SkatesLogic.class);


    private StickDAO stickDAO;

    public StickLogic() {
        stickDAO = new StickDAO();
    }

    public void save(Stick stick) throws DataBaseException {
        stickDAO.save(stick);
    }

    public Stick update(Stick stick) throws DataBaseException {
        return stickDAO.update(stick);
    }

    public Stick getById(int id) throws DataBaseException {
        return stickDAO.getById(id);
    }

    public List<Stick> getAll() throws DataBaseException {
        return stickDAO.getAll();
    }

    public List<Stick> getContactsSticks(int contactId) throws DataBaseException {
        return stickDAO.getContactsSticks(contactId);
    }

    public void delete(Stick stick) throws DataBaseException {
        stickDAO.delete(stick);
    }

}
