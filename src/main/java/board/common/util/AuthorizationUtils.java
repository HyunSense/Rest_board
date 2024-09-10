package board.common.util;

import board.exception.UnauthorizedUserException;

public class AuthorizationUtils {

    public static void validateMemberAuthorization(Long entityMemberId, Long requestMemberId) {
        if (!entityMemberId.equals(requestMemberId)) {
            throw new UnauthorizedUserException("삭제할 권한이 없습니다.");
        }
    }

}
