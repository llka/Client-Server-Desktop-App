package ru.sportequipment.client.client;

import ru.sportequipment.entity.CommandResponse;

import java.util.Stack;

public class ContextHolder {
    private static Client client;
    private static Thread server;

    private static Stack<CommandResponse> responseStack;

    public ContextHolder() {
        responseStack = new Stack<>();
    }

    public static Client getClient() {
        return client;
    }

    public static void setClient(Client client) {
        ContextHolder.client = client;
    }

    public static Thread getServer() {
        return server;
    }

    public static void setServer(Thread server) {
        ContextHolder.server = server;
    }

    public static Stack<CommandResponse> getResponseStack() {
        return responseStack;
    }

    public static void setResponseStack(Stack<CommandResponse> response) {
        responseStack = response;
    }
}
