package ru.sportequipment.builder;


import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.server.Server;

public interface ServerBuilderInterface {
    Server build(String[] arguments) throws ApplicationException;
}
