package ru.sportequipment.command.admin;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.dto.StickListDTO;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Session;
import ru.sportequipment.entity.Stick;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.logic.StickLogic;
import ru.sportequipment.util.JsonUtil;

public class UpdateStickCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(UpdateStickCommand.class);

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) throws ApplicationException {
        StickLogic stickLogic = new StickLogic();

        Stick stick = JsonUtil.deserialize(request.getBody(), Stick.class);
        stickLogic.update(stick);
        StickListDTO stickListDTO = new StickListDTO(stickLogic.getAll());
        return new CommandResponse(JsonUtil.serialize(stickListDTO), ResponseStatus.OK);
    }
}

