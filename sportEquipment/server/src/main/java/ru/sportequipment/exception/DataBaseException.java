package ru.sportequipment.exception;

import ru.sportequipment.entity.enums.ResponseStatus;

public class DataBaseException extends ApplicationException {
    public DataBaseException(ResponseStatus status) {
        super(status);
    }

    public DataBaseException(String message, ResponseStatus status) {
        super(message, status);
    }

    public DataBaseException(String message, Throwable cause, ResponseStatus status) {
        super(message, cause, status);
    }

    public DataBaseException(Throwable cause, ResponseStatus status) {
        super(cause, status);
    }
}
