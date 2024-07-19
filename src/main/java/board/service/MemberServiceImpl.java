package board.service;

import board.common.Role;
import board.dto.request.auth.SignUpRequestDto;
import board.dto.response.ResponseDto;
import board.dto.response.auth.SignUpResponseDto;
import board.entity.Member;
import board.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {


    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//    private final String ROLE_USER = "ROLE_USER";


    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {

        try {
            if (userMapper.existsByUsername(dto.getUsername())) {
                return SignUpResponseDto.existLoginId();
            }


            if (userMapper.existsByEmail(dto.getEmail())) {
                return SignUpResponseDto.existEmail();
            }

            String rawPassword = dto.getPassword();
            String password = bCryptPasswordEncoder.encode(rawPassword);

            Member member = Member.builder()
                    .username(dto.getUsername())
                    .password(password)
                    .name(dto.getName())
                    .email(dto.getEmail())
                    .role("ROLE_USER")
                    .build();

            userMapper.save(member);

            return SignUpResponseDto.success();

        } catch (Exception e) {
            log.warn("signUp exception = ", e);
            return ResponseDto.databaseError();
        }
    }

    @Override
    public Member login(String username) {



        return null;
    }

}
