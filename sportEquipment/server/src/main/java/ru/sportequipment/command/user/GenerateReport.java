package ru.sportequipment.command.user;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Contact;
import ru.sportequipment.entity.Session;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.entity.enums.RoleEnum;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.logic.ContactLogic;
import ru.sportequipment.util.ExcelUtil;

import java.util.ArrayList;
import java.util.List;

public class GenerateReport implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(GenerateReport.class);

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) throws ApplicationException {
        ContactLogic contactService = new ContactLogic();
        List<Contact> contacts = new ArrayList<>();
        if (RoleEnum.USER.equals(session.getVisitor().getRole())) {
            contacts.add(session.getVisitor().getContact());
        } else if (RoleEnum.ADMIN.equals(session.getVisitor().getRole())) {
            contacts.addAll(contactService.getAll());
        }
        ExcelUtil.generateExcelReport(contactService.getAll(), "Equipment report", request.getParameter("path"));
        return new CommandResponse(ResponseStatus.OK);
    }
}
