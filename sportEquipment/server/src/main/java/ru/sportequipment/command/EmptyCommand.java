package ru.sportequipment.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Session;

public class EmptyCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(EmptyCommand.class);

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) {
        logger.debug("Welcome to empty command");

        return new CommandResponse(CommandType.EMPTY.toString());
    }
}
