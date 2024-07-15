package board.exception;

import board.dto.response.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BadRequestExceptionHandler {


//    @ExceptionHandler
//    public ResponseEntity<ResponseDto> validationFailed() {
//
//        return ResponseDto.validationFailed();
//    }

}
