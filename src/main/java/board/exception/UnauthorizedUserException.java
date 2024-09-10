package board.exception;

public class UnauthorizedUserException extends RuntimeException {

    private final String message;

    public UnauthorizedUserException(String message) {
        super(message);
        this.message = message;
    }
}
