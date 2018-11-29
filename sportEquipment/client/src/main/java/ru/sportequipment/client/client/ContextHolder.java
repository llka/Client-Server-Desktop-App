package ru.sportequipment.client.client;

import ru.sportequipment.entity.CommandResponse;
import ru.sportequipment.entity.Session;

import java.util.ArrayDeque;
import java.util.Deque;

public class ContextHolder {
    private static Client client;
    private static Thread server;
    private static Session session;

    private static Deque<CommandResponse> responseStack;

    public ContextHolder() {
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

    public static Deque<CommandResponse> getResponseStack() {
        if (responseStack == null) {
            responseStack = new ArrayDeque<>();
        }
        return responseStack;
    }

    public static void setResponseStack(Deque<CommandResponse> stack) {
        responseStack = stack;
    }

    public static Session getSession() {
        return session;
    }

    public static void setSession(Session session) {
        ContextHolder.session = session;
    }
}
