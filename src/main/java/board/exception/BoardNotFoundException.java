package board.exception;

import lombok.Getter;

@Getter
public class BoardNotFoundException extends RuntimeException{

    private final String message;

    public BoardNotFoundException(String message) {
        super(message);
        this.message = message;
    }

}
