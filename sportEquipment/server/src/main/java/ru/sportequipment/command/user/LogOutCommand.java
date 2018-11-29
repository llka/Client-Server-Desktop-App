package ru.sportequipment.command.user;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Session;
import ru.sportequipment.entity.Visitor;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.entity.enums.RoleEnum;

public class LogOutCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(LogOutCommand.class);

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) {

        session.setVisitor(new Visitor(RoleEnum.GUEST));
        session.getVisitor().setContact(null);

        return new CommandResponse(ResponseStatus.OK);
    }
}
