package board.controller;

import board.dto.MemberDto;
import board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/")
    public String home() {

        log.info("IndexController - home");

        return "home";
    }

    @PostMapping("/sign-up")
    public String signUp(@RequestBody MemberDto memberDto) {

        memberService.signUp(memberDto);

        return "success";
    }
}

