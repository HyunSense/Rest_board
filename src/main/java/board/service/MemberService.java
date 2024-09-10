package board.service;

import board.dto.request.auth.SignUpRequestDto;
import board.dto.response.ResponseDto;

public interface MemberService {

    ResponseDto signUp(SignUpRequestDto dto);
}
