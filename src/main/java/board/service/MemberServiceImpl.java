package board.service;

import board.dto.Member;
import board.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final UserMapper userMapper;

    @Override
    public void join(Member member) {
        userMapper.save(member);
    }

    @Override
    public Member login(String username) {

        return userMapper.findByUserName(username);
    }
}
