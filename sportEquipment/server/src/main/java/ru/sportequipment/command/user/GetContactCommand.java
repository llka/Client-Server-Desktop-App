package ru.sportequipment.command.user;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Contact;
import ru.sportequipment.entity.Session;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.logic.ContactLogic;
import ru.sportequipment.util.JsonUtil;

public class GetContactCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(GetContactCommand.class);

    private static final String EMAIL_PARAM = "email";
    private static final String ID_PARAM = "id";

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) throws ApplicationException {
        ContactLogic contactLogic = new ContactLogic();
        String email = request.getParameter(EMAIL_PARAM);
        int id = -1;
        if (request.getParameter(ID_PARAM) != null) {
            id = Integer.parseInt(request.getParameter(ID_PARAM));
        }
        Contact contact = null;
        if (email != null && !email.isEmpty()) {
            try {
                contact = contactLogic.getByEmail(email);
            } catch (ApplicationException e) {
                if (id > 0) {
                    contact = contactLogic.getById(id);
                }
            }
        } else if (id > 0) {
            try {
                contact = contactLogic.getById(id);
            } catch (ApplicationException e) {
                if (id > 0) {
                    contact = contactLogic.getByEmail(email);
                }
            }
        }
        if (contact != null) {
            logger.debug(contact);
            return new CommandResponse(JsonUtil.serialize(contact), ResponseStatus.OK);
        } else {
            return new CommandResponse("Contact not found", ResponseStatus.NOT_FOUND);
        }
    }
}