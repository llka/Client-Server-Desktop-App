package ru.sportequipment.entity;

import java.util.HashMap;
import java.util.Map;

public class CommandRequest {
    private String command;
    private String body;
    private Map<String, String> parameters;

    public CommandRequest() {
        parameters = new HashMap<>();
    }

    public CommandRequest(String command) {
        this.command = command;
        parameters = new HashMap<>();
    }

    public CommandRequest(String command, String body) {
        this.command = command;
        this.body = body;
        parameters = new HashMap<>();
    }

    public CommandRequest(String command, String body, Map<String, String> parameters) {
        this.command = command;
        this.body = body;
        this.parameters = parameters;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(String key, String value) {
        parameters.put(key, value);
    }

    public void removeParameter(String key) {
        parameters.remove(key);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }
}