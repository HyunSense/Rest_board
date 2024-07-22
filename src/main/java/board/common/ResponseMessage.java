package board.common;

public interface ResponseMessage {

    // Http Status 200
    String SUCCESS = "Success";

    // Http Status 404
    String NOT_EXIST_RESULT = "No search results found";

    // Http Status 400
    String VALIDATION_FAILED = "Validation failed";
    String DUPLICATE_USERNAME = "Duplicate username";
    String DUPLICATE_EMAIL = "Duplicate email";
    String NOT_EXIST_USER = "User does not exist";
    String NOT_EXIST_BOARD = "Board does not exist";
    String NOT_EXIST_COMMENT = "Comment does not exist";

    // Http Status 401
    String LOGIN_FAILED = "Login failed. Invalid username or password format";
    String TOKEN_EXPIRED = "Token expired";
    String INVALID_TOKEN = "Invalid token";

    // Http status 403
    String PERMISSION_DENIED = "Permission denied";

    // Http Status 500
    String DATABASE_ERROR = "Database error";

}
