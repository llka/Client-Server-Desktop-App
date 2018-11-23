package ru.sportequipment.command.guest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.command.CommandType;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;

public class LogInCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(LogInCommand.class);

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response) {
        return new CommandResponse(CommandType.LOGIN.toString(), "logged in");
    }
}
