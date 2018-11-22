package ru.sportequipment.builder;


import ru.sportequipment.exception.BuilderException;
import ru.sportequipment.server.Server;

public interface ServerBuilderInterface {
    Server build(String[] arguments) throws BuilderException;
}
