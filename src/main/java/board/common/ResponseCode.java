package board.common;

public interface ResponseCode {

    // Http Status 200
    String SUCCESS = "SU";

    // Http Status 400

    String VALIDATION_FAILED = "VF";
    String DUPLICATE_LOGIN_ID = "DL";
    String DUPLICATE_EMAIL = "DE";
    String NOT_EXIST_USER = "NU";
    String NOT_EXIST_BOARD = "NB";

    // Http Status 401
    String LOGIN_FAILED = "LF";
    String TOKEN_EXPIRED = "TE";
    String INVALID_TOKEN = "IT";

    // Http status 403
    String PERMISSION_DENIED = "PD";

    // Http Status 500
    String DATABASE_ERROR = "DE";

}
