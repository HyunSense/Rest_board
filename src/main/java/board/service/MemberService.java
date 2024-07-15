package board.service;

import board.dto.MemberDto;
import board.entity.Member;

public interface MemberService {

    void signUp(MemberDto memberDto);

//    Member login(String username);

}
