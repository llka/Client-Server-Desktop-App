package ru.sportequipment.command.user;

import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.command.CommandType;
import ru.sportequipment.entity.*;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.logic.BookingLogic;
import ru.sportequipment.logic.ContactLogic;
import ru.sportequipment.logic.StickLogic;
import ru.sportequipment.util.JsonUtil;

public class BookSticksCommand implements ActionCommand {

    private static final String CONTACT_ID_PARAM = "contactId";
    private static final String STICK_ID_PARAM = "stickId";
    private static final String HOURS_PARAM = "hours";


    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) throws ApplicationException {
        ContactLogic contactLogic = new ContactLogic();
        BookingLogic bookingLogic = new BookingLogic();
        StickLogic stickLogic = new StickLogic();

        int contactId = Integer.parseInt(request.getParameter(CONTACT_ID_PARAM));
        int stickId = Integer.parseInt(request.getParameter(STICK_ID_PARAM));
        int hours = Integer.parseInt(request.getParameter(HOURS_PARAM));


        Contact contact = contactLogic.getById(contactId);
        Stick stick = stickLogic.getById(stickId);
        contact = bookingLogic.bookEquipment(contact, stick, hours);

        return new CommandResponse(CommandType.BOOK_STICK.toString(), JsonUtil.serialize(contact), ResponseStatus.OK);
    }
}
