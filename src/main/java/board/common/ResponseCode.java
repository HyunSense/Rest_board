package board.common;

public interface ResponseCode {

    // Http Status 200
    String SUCCESS = "SU";

    // Http Status 404
    String NOT_EXIST_RESULT = "NER";

    // Http Status 400
    String VALIDATION_FAILED = "VF";
    String DUPLICATE_USERNAME = "DU";
    String DUPLICATE_EMAIL = "DE";
    String NOT_EXIST_USER = "NU";
    String NOT_EXIST_BOARD = "NB";
    String NOT_EXIST_COMMENT = "NC";

    // Http Status 401
    String LOGIN_FAILED = "LF";
    String TOKEN_EXPIRED = "TE";
    String INVALID_TOKEN = "IT";

    // Http status 403
    String PERMISSION_DENIED = "PD";

    // Http Status 500
    String DATABASE_ERROR = "DBE";

}
