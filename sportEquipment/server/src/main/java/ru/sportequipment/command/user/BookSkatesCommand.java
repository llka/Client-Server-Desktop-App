package ru.sportequipment.command.user;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.dto.SkatesListDTO;
import ru.sportequipment.entity.*;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.logic.BookingLogic;
import ru.sportequipment.logic.SkatesLogic;
import ru.sportequipment.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

public class BookSkatesCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(BookSkatesCommand.class);

    private static final String HOURS_PARAM = "hours";

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) throws ApplicationException {
        SkatesLogic skatesLogic = new SkatesLogic();
        BookingLogic bookingLogic = new BookingLogic();

        List<Skates> skatesList = new ArrayList<>();

        if (request.getParameter(HOURS_PARAM) == null) {
            return new CommandResponse("Please define booking hours!", ResponseStatus.BAD_REQUEST);
        }

        if (request.getParameters() != null) {
            skatesList = skatesLogic.findSkates(request.getParameters());
            if (skatesList.size() == 1) {
                Skates bookedSkates = skatesList.get(0);
                Contact currentContact = bookingLogic.bookEquipment(session.getVisitor().getContact(), bookedSkates, Integer.parseInt(request.getParameter(HOURS_PARAM)));
                session.getVisitor().setContact(currentContact);

                logger.debug("contact " + currentContact);

                skatesList = new ArrayList<>();
                skatesList.add(skatesLogic.getById(bookedSkates.getId()));

                logger.debug(skatesList);

                SkatesListDTO skatesListDTO = new SkatesListDTO();
                skatesListDTO.setSkatesList(skatesList);

                return new CommandResponse(JsonUtil.serialize(skatesListDTO), ResponseStatus.OK);
            } else {
                return new CommandResponse("Please define skates!", ResponseStatus.BAD_REQUEST);
            }
        } else {
            return new CommandResponse("Skates not found", ResponseStatus.NOT_FOUND);
        }
    }
}
