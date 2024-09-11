package board.service.V2;

import board.common.ResponseCode;
import board.dto.request.auth.SignUpRequestDto;
import board.dto.response.ResponseDto;
import board.entity.V2.Member;
import board.repository.MemberRepository;
import board.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberServiceImpl implements MemberService {

    private final static String ROLE_USER = "ROLE_USER";

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseDto signUp(SignUpRequestDto dto) {

        if (memberRepository.existsByUsername(dto.getUsername())) {
            return ResponseDto.failure(ResponseCode.DUPLICATE_USERNAME, HttpStatus.BAD_REQUEST);
        }

        if (memberRepository.existsByEmail(dto.getEmail())) {
            return ResponseDto.failure(ResponseCode.DUPLICATE_EMAIL, HttpStatus.BAD_REQUEST);
        }

        String rawPassword = dto.getPassword();
        String password = bCryptPasswordEncoder.encode(rawPassword);

        Member member = Member.builder()
                .username(dto.getUsername())
                .password(password)
                .name(dto.getName())
                .email(dto.getEmail())
                .role(ROLE_USER)
                .build();

        memberRepository.save(member);

        return ResponseDto.success();
    }
}
