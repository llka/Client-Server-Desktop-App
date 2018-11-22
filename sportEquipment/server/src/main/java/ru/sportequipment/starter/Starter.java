package ru.sportequipment.starter;

import org.apache.log4j.Logger;
import ru.sportequipment.builder.ServerBuilder;
import ru.sportequipment.exception.BuilderException;
import ru.sportequipment.exception.ServerException;
import ru.sportequipment.server.Server;

public class Starter {
    private static Logger logger = Logger.getLogger(Starter.class);

    public static void main(String[] args) {
        try {
            Server server = new ServerBuilder().build(args);
            startServer(server);
        } catch (BuilderException e) {
            logger.error("Can not create server. " + e);
        }
    }

    private static void startServer(Server server) {
        try {
            server.start();
        } catch (ServerException e) {
            logger.error("Can not start server. " + e);
        }
    }

}
