package board.service;

import board.dto.request.auth.SignUpRequestDto;
import board.dto.response.auth.SignUpResponseDto;
import board.entity.Member;
import org.springframework.http.ResponseEntity;

public interface MemberService {

    ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto);

    Member login(String username);

}
