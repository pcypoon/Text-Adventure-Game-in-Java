package edu.uob;

import java.io.Serial;

public class GameTokenizeException extends Throwable {
    @Serial
    private static final long serialVersionUID = -1966799517817170397L;
    String errorMessage;

    public GameTokenizeException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String toString() {
        return errorMessage;
    }
}
