package board.exception;

import lombok.Getter;

@Getter
public class CommentNotFoundException extends RuntimeException{

    private final String message;

    public CommentNotFoundException(String message) {
        super(message);
        this.message = message;
    }

}
