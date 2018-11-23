package ru.sportequipment.entity;

import ru.sportequipment.entity.enums.ResponseStatus;

public class CommandResponse {
    private String fromCommand;
    private String body;
    private ResponseStatus status;

    public CommandResponse() {
    }

    public CommandResponse(ResponseStatus status) {
        this.status = status;
    }

    public CommandResponse(String body, ResponseStatus status) {
        this.body = body;
        this.status = status;
    }

    public CommandResponse(String fromCommand, String body, ResponseStatus status) {
        this.fromCommand = fromCommand;
        this.body = body;
        this.status = status;
    }

    public CommandResponse(String fromCommand) {
        this.fromCommand = fromCommand;
    }

    public CommandResponse(String fromCommand, String body) {
        this.fromCommand = fromCommand;
        this.body = body;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public String getFromCommand() {
        return fromCommand;
    }

    public void setFromCommand(String fromCommand) {
        this.fromCommand = fromCommand;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
