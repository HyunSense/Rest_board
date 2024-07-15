package board.service;

import board.dto.MemberDto;
import board.entity.Member;
import board.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public void signUp(MemberDto dto) {

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
    }
}
