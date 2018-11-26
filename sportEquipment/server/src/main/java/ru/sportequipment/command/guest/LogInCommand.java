package ru.sportequipment.command.guest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.command.CommandType;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Contact;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.LogicException;
import ru.sportequipment.logic.ContactLogic;
import ru.sportequipment.util.JsonUtil;

public class LogInCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(LogInCommand.class);

    private static final String EMAIL_PARAM = "email";
    private static final String PASSWORD_PARAM = "password";

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response) {
        ContactLogic contactLogic = new ContactLogic();

        String email = request.getParameter(EMAIL_PARAM);
        String password = request.getParameter(PASSWORD_PARAM);
        try {
            Contact contact = contactLogic.login(email, password);
            return new CommandResponse(CommandType.LOGIN.toString(), JsonUtil.serialize(contact), ResponseStatus.OK);
        } catch (LogicException e) {
            return new CommandResponse(CommandType.LOGIN.toString(), e.getMessage(), ResponseStatus.BAD_REQUEST);
        }
    }
}
