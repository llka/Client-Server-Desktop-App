package ru.sportequipment.factory;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.command.CommandType;
import ru.sportequipment.command.EmptyCommand;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.Visitor;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;

public class ActionFactory {
    private static final Logger logger = LogManager.getLogger(ActionFactory.class);

    public ActionCommand defineCommand(CommandRequest request, Visitor visitor) throws ApplicationException {

        ActionCommand command = new EmptyCommand();

        if (request == null || request.getCommand() == null) {
            throw new ApplicationException("Request is null!", ResponseStatus.BAD_REQUEST);
        }

        CommandType commandType = CommandType.valueOf(request.getCommand());
        if (commandType == null) {
            return command;
        }

        if (!commandType.role.contains(visitor.getRole())) {
            throw new ApplicationException("You don't have enough permissions for this action!", ResponseStatus.UNAUTHORIZED);
        }

        command = commandType.getCommand();
        logger.debug("ActionFactory command = " + command.getClass().toString());
        return command;
    }
}
