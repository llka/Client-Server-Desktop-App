package ru.sportequipment.client.client;

public class ConnectionHolder {
    private static Client client;
    private static Thread server;

    public ConnectionHolder() {
    }

    public static Client getClient() {
        return client;
    }

    public static void setClient(Client client) {
        ConnectionHolder.client = client;
    }

    public static Thread getServer() {
        return server;
    }

    public static void setServer(Thread server) {
        ConnectionHolder.server = server;
    }
}
