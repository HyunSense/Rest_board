package board.config.auth;

import board.entity.V1.Member;
import board.repository.V1.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("PrincipalDetailsService.loadUserByUsername");


        Member member = userMapper.findByUsername(username);
        log.info("member = {}", member);

        if (member == null) {
            throw new UsernameNotFoundException("Login failed");
        }

        return new PrincipalDetails(member);
    }
}
