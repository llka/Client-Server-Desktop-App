package ru.sportequipment.logic;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.dao.EquipmentDAO;
import ru.sportequipment.dao.SkatesDAO;
import ru.sportequipment.entity.Contact;
import ru.sportequipment.entity.Skates;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.exception.DataBaseException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SkatesLogic {
    private static final Logger logger = LogManager.getLogger(SkatesLogic.class);


    private SkatesDAO skatesDAO;
    private EquipmentDAO equipmentDAO;

    public SkatesLogic() {
        skatesDAO = new SkatesDAO();
        equipmentDAO = new EquipmentDAO();
    }

    public void save(Skates skates) throws DataBaseException {
        skatesDAO.save(skates);
    }

    public Skates update(Skates skates) throws ApplicationException {
        skatesDAO.getById(skates.getId());
        return skatesDAO.update(skates);
    }

    public Skates getById(int id) throws DataBaseException {
        return skatesDAO.getById(id);
    }

    public List<Skates> getAll() throws DataBaseException {
        return skatesDAO.getAll();
    }

    public List<Skates> findSkates(Map<String, String> filterParams) throws ApplicationException {
        if (filterParams == null || filterParams.isEmpty()) {
            return skatesDAO.getAll();
        } else {
            return skatesDAO.findSkates(filterParams);
        }
    }

    public List<Skates> refreshBookingInfo() throws DataBaseException {
        List<Skates> skatesList = skatesDAO.getAll();
        Date now = new Date();
        for (Skates skates : skatesList) {
            if (skates.getBookedTo() != null) {
                if (skates.getBookedTo().before(now)) {
                    skates.setBookedFrom(null);
                    skates.setBookedTo(null);
                    skatesDAO.update(skates);
                    equipmentDAO.refreshBookingInfoForSkates(skates);
                }
            }
        }

        return skatesDAO.getAll();
    }

    public List<Skates> getContactsSkates(int contactId) throws DataBaseException {
        return skatesDAO.getContactsSkates(contactId);
    }

    public void delete(Skates skates) throws DataBaseException {
        skatesDAO.getById(skates.getId());
        skatesDAO.delete(skates);
    }
}
