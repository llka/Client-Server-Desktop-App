package ru.sportequipment.command.user;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.command.CommandType;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Contact;
import ru.sportequipment.entity.Session;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.logic.ContactLogic;
import ru.sportequipment.util.JsonUtil;

public class CreateContactCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(CreateContactCommand.class);

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) throws ApplicationException {
        ContactLogic contactLogic = new ContactLogic();

        Contact contact = JsonUtil.deserialize(request.getBody(), Contact.class);
        try {
            contactLogic.getByEmail(contact.getEmail());
            return new CommandResponse(CommandType.CREATE_CONTACT.toString(), "Contact with the same email " + contact.getEmail() + " already exists!", ResponseStatus.PARTIAL_CONTENT);
        } catch (ApplicationException e) {
            logger.debug("no contacts with such email " + contact.getEmail());
            contact = contactLogic.register(contact);
            return new CommandResponse(CommandType.CREATE_CONTACT.toString(), JsonUtil.serialize(contact), ResponseStatus.OK);
        }
    }
}
