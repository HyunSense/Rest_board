package board.exception;

import board.common.ResponseCode;
import board.dto.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ResponseDto> handleValidationException(Exception e) {

        log.warn(e.getMessage(), e);
        ResponseDto failureResponse = ResponseDto.failure(ResponseCode.VALIDATION_FAILED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failureResponse);
    }

    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<ResponseDto> handleBoardNotFoundException(BoardNotFoundException e) {

        log.warn(e.getMessage(), e);
        ResponseDto failureResponse = ResponseDto.failure(ResponseCode.NOT_EXIST_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failureResponse);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ResponseDto> handleCommentNotFoundException(CommentNotFoundException e) {

        log.warn(e.getMessage(), e);
        ResponseDto failureResponse = ResponseDto.failure(ResponseCode.NOT_EXIST_COMMENT);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failureResponse);
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<ResponseDto> handleUnauthorizedException(UnauthorizedUserException e) {

        log.warn(e.getMessage(), e);
        ResponseDto failureResponse = ResponseDto.failure(ResponseCode.PERMISSION_DENIED);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(failureResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto> exception(Exception e) {

        log.warn("exception", e);
        ResponseDto failureResponse = ResponseDto.failure(ResponseCode.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(failureResponse);
    }

}
