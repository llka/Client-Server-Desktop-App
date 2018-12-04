package ru.sportequipment.command.guest;

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

public class RegisterCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(RegisterCommand.class);

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) throws ApplicationException {
        ContactLogic contactLogic = new ContactLogic();

        Contact contact = JsonUtil.deserialize(request.getBody(), Contact.class);
        contact = contactLogic.register(contact);

        session.getVisitor().setContact(contact);
        session.getVisitor().setRole(contact.getRole());
        return new CommandResponse(CommandType.REGISTER.toString(), JsonUtil.serialize(contact), ResponseStatus.OK);

    }
}