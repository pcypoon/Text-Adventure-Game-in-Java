package edu.uob;
import java.io.Serial;

public class GameAdditionalCmdException extends Throwable {
    @Serial
    private static final long serialVersionUID = -6916847369870850001L;
    String errorMessage;

    public GameAdditionalCmdException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String toString() {
        return errorMessage;
    }
}
