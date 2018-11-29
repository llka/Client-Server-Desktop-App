package ru.sportequipment.command.user;

import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.command.CommandType;
import ru.sportequipment.entity.*;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.logic.BookingLogic;
import ru.sportequipment.logic.ContactLogic;
import ru.sportequipment.logic.SkatesLogic;
import ru.sportequipment.util.JsonUtil;

public class BookSkatesCommand implements ActionCommand {

    private static final String CONTACT_ID_PARAM = "contactId";
    private static final String SKATES_ID_PARAM = "skatesId";
    private static final String HOURS_PARAM = "hours";


    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) throws ApplicationException {
        ContactLogic contactLogic = new ContactLogic();
        BookingLogic bookingLogic = new BookingLogic();
        SkatesLogic skatesLogic = new SkatesLogic();

        int contactId = Integer.parseInt(request.getParameter(CONTACT_ID_PARAM));
        int skatesId = Integer.parseInt(request.getParameter(SKATES_ID_PARAM));
        int hours = Integer.parseInt(request.getParameter(HOURS_PARAM));


        Contact contact = contactLogic.getById(contactId);
        Skates skates = skatesLogic.getById(skatesId);
        contact = bookingLogic.bookEquipment(contact, skates, hours);

        return new CommandResponse(CommandType.BOOK_SKATES.toString(), JsonUtil.serialize(contact), ResponseStatus.OK);
    }
}
