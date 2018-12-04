package ru.sportequipment.command.admin;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Session;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.logic.ContactLogic;

public class DeleteContactCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(DeleteContactCommand.class);

    private static final String EMAIL_PARAM = "email";
    private static final String ID_PARAM = "id";

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) throws ApplicationException {
        ContactLogic contactLogic = new ContactLogic();
        contactLogic.delete(request.getParameter(ID_PARAM), request.getParameter(EMAIL_PARAM));

        return new CommandResponse(ResponseStatus.OK);
    }
}
