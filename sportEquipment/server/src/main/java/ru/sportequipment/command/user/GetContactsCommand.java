package ru.sportequipment.command.user;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.sportequipment.command.ActionCommand;
import ru.sportequipment.command.CommandType;
import ru.sportequipment.dto.ContactsDTO;
import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Contact;
import ru.sportequipment.entity.Session;
import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.logic.ContactLogic;
import ru.sportequipment.util.JsonUtil;

import java.util.List;

public class GetContactsCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(GetContactsCommand.class);

    @Override
    public CommandResponse execute(CommandRequest request, CommandResponse response, Session session) throws ApplicationException {
        ContactLogic contactLogic = new ContactLogic();

        List<Contact> contacts = contactLogic.getAll();
        ContactsDTO contactsDTO = new ContactsDTO();
        contactsDTO.setContacts(contacts);
        logger.debug("all contacts " + contacts);
        return new CommandResponse(CommandType.GET_CONTACTS.toString(), JsonUtil.serialize(contactsDTO), ResponseStatus.OK);
    }
}