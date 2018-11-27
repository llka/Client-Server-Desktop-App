package ru.sportequipment.client.controller;

import ru.sportequipment.client.client.ContextHolder;
import ru.sportequipment.entity.CommandResponse;

import java.util.EmptyStackException;
import java.util.NoSuchElementException;

public class Controller {

    public static CommandResponse getLastResponse() {
        while (true) {
            try {
                CommandResponse response = ContextHolder.getResponseStack().pop();
                if (response != null) {
                    return response;
                }
            } catch (EmptyStackException | NoSuchElementException e) {

            }
        }
    }
}
