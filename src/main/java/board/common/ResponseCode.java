package board.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

    // status 200
    SUCCESS("SU", "Success"),

    // status 400
    DUPLICATE_USERNAME("DU", "Duplicate username"),
    DUPLICATE_EMAIL("DE", "Duplicate email"),
    VALIDATION_FAILED("VF", "Validation failed"),
    NOT_EXIST_USER("NU", "Not exists user"),
    NOT_EXIST_BOARD("NB", "Not exists board"),
    NOT_EXIST_COMMENT("NC", "Not exists comment"),

    // status 401
    LOGIN_FAILED("LF", "Login failed"),
    TOKEN_EXPIRED("TE", "Token expired"),
    INVALID_TOKEN("IT", "Invalid token"),

    // status 403
    PERMISSION_DENIED("PD", "Permission denied"),

    // status 500
    DATABASE_ERROR("DE", "Database error");


    private final String code;
    private final String message;

}
