package board.common;

public interface ResponseMessage {

    // Http Status 200
    String SUCCESS = "Success";

    // Http Status 400
    String DUPLICATE_LOGIN_ID = "Duplicate loginId";
    String DUPLICATE_EMAIL = "Duplicate email";
    String NOT_EXIST_BOARD = "Board does not exist";

    // Http Status 401
    String LOGIN_FAIL = "Login Failed. Invalid loginId or password format";

    // Http status 403
    String PERMISSION_DENIED = "Permission denied";

    // Http Status 500
    String DATABASE_ERROR = "Database error";

}
