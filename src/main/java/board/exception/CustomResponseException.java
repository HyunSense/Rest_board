package board.exception;

import lombok.Getter;

@Getter
public class CustomResponseException extends RuntimeException {


    private final String responseCode;
    private final String responseMessage;

    public CustomResponseException(String responseCode, String responseMessage) {
        super(responseMessage);
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }
}
