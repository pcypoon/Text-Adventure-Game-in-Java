package edu.uob;

import java.io.Serial;

public class GameInterpretException extends Throwable {
    @Serial
    private static final long serialVersionUID = -6595161140799436837L;
    String errorMessage;

    public GameInterpretException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String toString() {
        return errorMessage;
    }
}
