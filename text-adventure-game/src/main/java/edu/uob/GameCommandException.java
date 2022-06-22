package edu.uob;
import java.io.Serial;

public class GameCommandException extends Throwable {
    @Serial
    private static final long serialVersionUID = 2541624846609925832L;
    String errorMessage;

    public GameCommandException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String toString() {
        return errorMessage;
    }
}
