package board.service;

import board.dto.Member;

public interface MemberService {

    void join(Member member);

    Member login(String username);

}
