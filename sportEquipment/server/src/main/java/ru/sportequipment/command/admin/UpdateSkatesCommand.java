package ru.sportequipment.command.admin;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Session;
import ru.sportequipment.entity.Skates;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.logic.SkatesLogic;
import ru.sportequipment.util.JsonUtil;

public class UpdateSkatesCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(UpdateSkatesCommand.class);

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) throws ApplicationException {
        SkatesLogic skatesLogic = new SkatesLogic();

        Skates skates = JsonUtil.deserialize(request.getBody(), Skates.class);
        skatesLogic.update(skates);
        return new CommandResponse(ResponseStatus.OK);
    }
}
