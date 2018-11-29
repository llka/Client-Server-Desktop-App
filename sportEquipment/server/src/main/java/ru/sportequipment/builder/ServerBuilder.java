package ru.sportequipment.builder;


import ru.sportequipment.entity.enums.ResponseStatus;
import ru.sportequipment.exception.ApplicationException;
import ru.sportequipment.server.Server;

public class ServerBuilder implements ServerBuilderInterface {
    private static final int ARG_PORT_NUMBER_INDEX = 0;

    @Override
    public Server build(String[] arguments) throws ApplicationException {
        Server server;
        switch (arguments.length) {
            case 1:
                try {
                    server = new Server(Integer.parseInt(arguments[ARG_PORT_NUMBER_INDEX]));
                } catch (NumberFormatException e) {
                    throw new ApplicationException("Invalid port number.", ResponseStatus.INTERNAL_SERVER_ERROR);
                }
                break;
            case 0:
                server = new Server();
                break;
            default:
                throw new ApplicationException("Invalid number of arguments!", ResponseStatus.INTERNAL_SERVER_ERROR);
        }
        return server;
    }
}
