package edu.uob;
import java.io.Serial;

public class GameParseException extends Throwable {
    @Serial
    private static final long serialVersionUID = -8540961016088684119L;
    String errorMessage;

    public GameParseException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String toString() {
        return errorMessage;
    }
}
