package ru.sportequipment.command.user;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.dto.StickListDTO;
import ru.sportequipment.entity.*;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.logic.BookingLogic;
import ru.sportequipment.logic.StickLogic;
import ru.sportequipment.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

public class BookStickCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(BookStickCommand.class);

    private static final String HOURS_PARAM = "hours";

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) throws ApplicationException {
        StickLogic stickLogic = new StickLogic();
        BookingLogic bookingLogic = new BookingLogic();

        List<Stick> stickList = new ArrayList<>();

        if (request.getParameter(HOURS_PARAM) == null) {
            return new CommandResponse("Please define booking hours!", ResponseStatus.BAD_REQUEST);
        }

        if (request.getParameters() != null) {
            stickList = stickLogic.findSticks(request.getParameters());
            if (stickList.size() == 1) {
                Stick bookedStick = stickList.get(0);
                Contact currentContact = bookingLogic.bookEquipment(session.getVisitor().getContact(), bookedStick, Integer.parseInt(request.getParameter(HOURS_PARAM)));
                session.getVisitor().setContact(currentContact);

                logger.debug("contact " + currentContact);

                stickList = new ArrayList<>();
                stickList.add(stickLogic.getById(bookedStick.getId()));

                logger.debug(stickList);

                StickListDTO stickListDTO = new StickListDTO();
                stickListDTO.setStickList(stickList);

                return new CommandResponse(JsonUtil.serialize(stickListDTO), ResponseStatus.OK);
            } else {
                return new CommandResponse("Please define stick!", ResponseStatus.BAD_REQUEST);
            }
        } else {
            return new CommandResponse("Stick not found", ResponseStatus.NOT_FOUND);
        }
    }
}