package board.service;

import board.dto.request.auth.SignUpRequestDto;
import board.dto.response.auth.SignUpResponseDto;
import board.entity.V1.Member;
import board.repository.V1.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {


    @Mock
    private UserMapper userMapper;
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
        given(userMapper.existsByUsername(validSignUpRequestDto.getUsername())).willReturn(false);
        given(userMapper.existsByEmail(validSignUpRequestDto.getEmail())).willReturn(false);
        given(bCryptPasswordEncoder.encode(validSignUpRequestDto.getPassword())).willReturn("encodedPassword");
        willDoNothing().given(userMapper).save(any(Member.class));

        // when
        ResponseEntity<? super SignUpResponseDto> response = memberService.signUp(validSignUpRequestDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userMapper).existsByUsername(validSignUpRequestDto.getUsername());
        verify(userMapper).existsByEmail(validSignUpRequestDto.getEmail());
        verify(bCryptPasswordEncoder).encode(validSignUpRequestDto.getPassword());
        verify(userMapper).save(any(Member.class));
    }

    @Test
    @DisplayName("Username 중복 회원가입 시도")
    void signUpDuplicatedUsername() {
        //given
        given(userMapper.existsByUsername(duplicatedUsernameSignUpRequestDto.getUsername())).willReturn(true);

        //when
        ResponseEntity<? super SignUpResponseDto> response = memberService.signUp(duplicatedUsernameSignUpRequestDto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(userMapper).existsByUsername(duplicatedUsernameSignUpRequestDto.getUsername());
    }

    @Test
    @DisplayName("Email 중복 회원가입 시도")
    void signUpDuplicatedEmail() {
        //given
        given(userMapper.existsByEmail(duplicatedEmailSignUpRequestDto.getEmail())).willReturn(true);

        //when
        ResponseEntity<? super SignUpResponseDto> response = memberService.signUp(duplicatedEmailSignUpRequestDto);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(userMapper).existsByUsername(duplicatedEmailSignUpRequestDto.getUsername());
    }
    
    @Test
    @DisplayName("회원가입 데이터베이스 에러")
    void SignUpDatabaseError() {
        //given
        given(userMapper.existsByUsername(validSignUpRequestDto.getUsername())).willReturn(false);
        given(userMapper.existsByEmail(validSignUpRequestDto.getEmail())).willReturn(false);
        given(bCryptPasswordEncoder.encode(validSignUpRequestDto.getPassword())).willReturn("encodedPassword");
        doThrow(new RuntimeException()).when(userMapper).save(any(Member.class));
//        willThrow(new RuntimeException()).given(userMapper).save(any(Member.class));

        //when
        ResponseEntity<? super SignUpResponseDto> response = memberService.signUp(validSignUpRequestDto);

        
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(userMapper).existsByUsername(validSignUpRequestDto.getUsername());
        verify(userMapper).existsByEmail(validSignUpRequestDto.getEmail());
        verify(bCryptPasswordEncoder).encode(validSignUpRequestDto.getPassword());
        verify(userMapper).save(any(Member.class));

    }
}