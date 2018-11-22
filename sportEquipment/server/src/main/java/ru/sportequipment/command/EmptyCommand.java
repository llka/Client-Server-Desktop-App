package ru.sportequipment.command;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;

public class EmptyCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(EmptyCommand.class);

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response) {
        logger.debug("Welcome to empty command");

        return new CommandResponse(CommandType.EMPTY);
    }
}
