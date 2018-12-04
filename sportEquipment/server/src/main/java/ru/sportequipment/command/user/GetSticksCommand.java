package ru.sportequipment.command.user;

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

import java.util.ArrayList;
import java.util.List;

public class GetSticksCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(GetSticksCommand.class);

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) throws ApplicationException {

        StickLogic stickLogic = new StickLogic();
        List<Stick> stickList = new ArrayList<>();

        if (request.getParameters() != null) {
            stickList = stickLogic.findSticks(request.getParameters());
        } else {
            stickList = stickLogic.getAll();
        }

        if (stickList != null) {
            StickListDTO stickListDTO = new StickListDTO();
            stickListDTO.setStickList(stickList);
            return new CommandResponse(JsonUtil.serialize(stickListDTO), ResponseStatus.OK);
        } else {
            return new CommandResponse("Sticks not found", ResponseStatus.NOT_FOUND);
        }
    }
}
