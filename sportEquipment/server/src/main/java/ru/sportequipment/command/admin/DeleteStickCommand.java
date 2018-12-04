package ru.sportequipment.command.admin;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Session;
import ru.sportequipment.entity.Stick;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.logic.StickLogic;

import java.util.List;

public class DeleteStickCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(DeleteStickCommand.class);

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) throws ApplicationException {
        StickLogic stickLogic = new StickLogic();

        if (request.getParameters() != null) {
            List<Stick> stickList = stickLogic.findSticks(request.getParameters());
            for (Stick stick : stickList) {
                if (stick.getBookedTo() != null) {
                    return new CommandResponse("Stick cannot be deleted because it is in use!", ResponseStatus.BAD_REQUEST);
                } else {
                    stickLogic.delete(stick);
                }
            }
            return new CommandResponse(ResponseStatus.OK);
        } else {
            return new CommandResponse("Define stick to be deleted!", ResponseStatus.BAD_REQUEST);
        }
    }
}
