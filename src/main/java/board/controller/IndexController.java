package board.controller;

import board.dto.Member;
import board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class IndexController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/")
    public String home() {

        log.info("IndexController - home");

        return "home";
    }

    @PostMapping("/join")
    public String join(@RequestBody Member member) {

        String rawPassword = member.getPassword();
        String password = bCryptPasswordEncoder.encode(rawPassword);
        member.setPassword(password);
        member.setRole("ROLE_USER");
        memberService.join(member);

        return "success";
    }

    @GetMapping("/user")
    public String user() {

        return "user";
    }

    @GetMapping("/admin")
    public String admin() {

        return "admin";
    }
}

