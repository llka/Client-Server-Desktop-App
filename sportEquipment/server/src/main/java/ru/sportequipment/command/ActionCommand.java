package ru.sportequipment.command;

import ru.sportequipment.entity.CommandRequest;
import ru.sportequipment.entity.CommandResponse;

public interface ActionCommand {
    CommandResponse execute(CommandRequest request, CommandResponse response);
}