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

import java.util.List;

public class DeleteSkatesCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(DeleteSkatesCommand.class);

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) throws ApplicationException {
        SkatesLogic skatesLogic = new SkatesLogic();

        if (request.getParameters() != null) {
            List<Skates> skatesList = skatesLogic.findSkates(request.getParameters());
            for (Skates skates : skatesList) {
                if (skates.getBookedTo() != null) {
                    return new CommandResponse("Skates cannot be deleted because they are in use!", ResponseStatus.BAD_REQUEST);
                } else {
                    skatesLogic.delete(skates);
                }
            }
            return new CommandResponse(ResponseStatus.OK);
        } else {
            return new CommandResponse("Define skates to be deleted!", ResponseStatus.BAD_REQUEST);
        }
    }
}
