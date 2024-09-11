package board.service.V2;

import board.common.ResponseCode;
import board.dto.request.auth.SignUpRequestDto;
import board.dto.response.ResponseDto;
import board.entity.V2.Member;
import board.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {


    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    private MemberServiceImpl memberService;


    private SignUpRequestDto validSignUpRequestDto;
    private SignUpRequestDto duplicatedUsernameSignUpRequestDto;
    private SignUpRequestDto duplicatedEmailSignUpRequestDto;

    @BeforeEach
    void setUp() {
        validSignUpRequestDto = new SignUpRequestDto();
        validSignUpRequestDto.setUsername("newuser");
        validSignUpRequestDto.setPassword("password");
        validSignUpRequestDto.setName("New User");
        validSignUpRequestDto.setEmail("newuser@example.com");

        duplicatedUsernameSignUpRequestDto = new SignUpRequestDto();
        duplicatedUsernameSignUpRequestDto.setUsername("duplicateUser");
        duplicatedUsernameSignUpRequestDto.setPassword("password");
        duplicatedUsernameSignUpRequestDto.setName("Existing User");
        duplicatedUsernameSignUpRequestDto.setEmail("newemail@example.com");

        duplicatedEmailSignUpRequestDto = new SignUpRequestDto();
        duplicatedEmailSignUpRequestDto.setUsername("newuser");
        duplicatedEmailSignUpRequestDto.setPassword("password");
        duplicatedEmailSignUpRequestDto.setName("New User");
        duplicatedEmailSignUpRequestDto.setEmail("duplicateEmail@example.com");
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUpSuccess() {
        // given
        given(memberRepository.existsByUsername(validSignUpRequestDto.getUsername())).willReturn(false);
        given(memberRepository.existsByEmail(validSignUpRequestDto.getEmail())).willReturn(false);
        given(bCryptPasswordEncoder.encode(validSignUpRequestDto.getPassword())).willReturn("encodedPassword");
        willDoNothing().given(memberRepository).save(any(Member.class));

        // when
        ResponseDto response = memberService.signUp(validSignUpRequestDto);

        // then
        assertThat(response.getCode()).isEqualTo(ResponseCode.SUCCESS.getCode());
        verify(memberRepository).existsByUsername(validSignUpRequestDto.getUsername());
        verify(memberRepository).existsByEmail(validSignUpRequestDto.getEmail());
        verify(bCryptPasswordEncoder).encode(validSignUpRequestDto.getPassword());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("Username 중복 회원가입 시도")
    void signUpDuplicatedUsername() {
        //given
        given(memberRepository.existsByUsername(duplicatedUsernameSignUpRequestDto.getUsername())).willReturn(true);

        //when
        ResponseDto response = memberService.signUp(duplicatedUsernameSignUpRequestDto);

        //then
        assertThat(response.getCode()).isEqualTo(ResponseCode.DUPLICATE_USERNAME.getCode());
        verify(memberRepository).existsByUsername(duplicatedUsernameSignUpRequestDto.getUsername());
    }

    @Test
    @DisplayName("Email 중복 회원가입 시도")
    void signUpDuplicatedEmail() {
        //given
        given(memberRepository.existsByEmail(duplicatedEmailSignUpRequestDto.getEmail())).willReturn(true);

        //when
        ResponseDto response = memberService.signUp(duplicatedEmailSignUpRequestDto);

        //then
        assertThat(response.getCode()).isEqualTo(ResponseCode.DUPLICATE_EMAIL.getCode());
        verify(memberRepository).existsByUsername(duplicatedEmailSignUpRequestDto.getUsername());
    }
}