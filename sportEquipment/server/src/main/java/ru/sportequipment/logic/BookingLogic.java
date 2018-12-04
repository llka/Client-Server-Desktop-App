package ru.sportequipment.logic;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.dao.ContactDAO;
import ru.sportequipment.dao.EquipmentDAO;
import ru.sportequipment.dao.SkatesDAO;
import ru.sportequipment.dao.StickDAO;
import ru.sportequipment.entity.Contact;
import ru.sportequipment.entity.Equipment;
import ru.sportequipment.entity.Skates;
import ru.sportequipment.entity.Stick;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.exception.DataBaseException;

import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;


public class BookingLogic {
    private static final Logger logger = LogManager.getLogger(BookingLogic.class);

    private EquipmentDAO equipmentDAO;
    private StickDAO stickDAO;
    private SkatesDAO skatesDAO;
    private ContactDAO contactDAO;
    private ContactLogic contactLogic;

    public BookingLogic() {
        this.equipmentDAO = new EquipmentDAO();
        this.contactDAO = new ContactDAO();
        this.skatesDAO = new SkatesDAO();
        this.stickDAO = new StickDAO();
        this.contactLogic = new ContactLogic();
    }

    public Contact bookEquipment(Contact contact, Stick stick, @Positive int hours) throws ApplicationException {
        Date now = new Date();
        if (stickDAO.getById(stick.getId()).getBookedTo().after(now)) {
            throw new ApplicationException("This equipment is already booked!", ResponseStatus.BAD_REQUEST);
        }
        prepareBookingInfo(stick, hours);
        stickDAO.update(stick);
        equipmentDAO.bookEquipment(contact, stick);

        return contactLogic.getById(contact.getId());
    }

    public Contact bookEquipment(Contact contact, Skates skates, @Positive int hours) throws ApplicationException {
        Date now = new Date();
        if (skates.getBookedTo() != null && skates.getBookedFrom() != null) {
            if (skates.getBookedTo().after(now)) {
                throw new ApplicationException("Skates are already booked!", ResponseStatus.BAD_REQUEST);
            }
        }

        logger.debug(skates);
        prepareBookingInfo(skates, hours);
        logger.debug("prepared skates " + skates);

        skatesDAO.update(skates);

        equipmentDAO.bookEquipment(contact, skates);
        return contactLogic.getById(contact.getId());
    }

    public void refreshContactsHaveSkates(Skates skates) {

    }


    public void refreshBookingInfo() throws DataBaseException {
        List<Skates> skatesList = skatesDAO.getAll();
        Date now = new Date();
        for (Skates skates : skatesList) {
            if (skates.getBookedTo() != null) {
                if (skates.getBookedTo().before(now)) {
                    skates.setBookedFrom(null);
                    skates.setBookedTo(null);
                    skatesDAO.update(skates);
                }
            }
        }

        List<Stick> sticks = stickDAO.getAll();
        for (Stick stick : sticks) {
            if (stick.getBookedTo() != null) {
                if (stick.getBookedTo().before(now)) {
                    stick.setBookedTo(null);
                    stick.setBookedFrom(null);
                    stickDAO.update(stick);
                }
            }
        }
    }


    private Equipment prepareBookingInfo(Equipment equipment, int hours) {
        Date now = new Date();
        Date bookingEndDate = DateUtils.addHours(now, hours);
        equipment.setBookedFrom(now);
        equipment.setBookedTo(bookingEndDate);
        return equipment;
    }

}
