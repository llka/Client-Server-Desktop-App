package ru.sportequipment.command.guest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.command.CommandType;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Contact;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.logic.ContactLogic;
import ru.sportequipment.util.JsonUtil;

public class RegisterCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(LogInCommand.class);

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response) {
        ContactLogic contactLogic = new ContactLogic();

        try {
            Contact contact = JsonUtil.deserialize(request.getBody(), Contact.class);
            contact = contactLogic.register(contact);
            return new CommandResponse(CommandType.LOGIN.toString(), JsonUtil.serialize(contact), ResponseStatus.OK);
        } catch (ApplicationException e) {
            return new CommandResponse(CommandType.LOGIN.toString(), e.getMessage(), ResponseStatus.BAD_REQUEST);
        }
    }
}