package board.exception;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomResponseException extends RuntimeException{

    private String responseCode;
    private String responseMessage;

    public CustomResponseException(String responseCode, String responseMessage) {


    }
}
