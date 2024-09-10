package board.controller;

import board.common.ResponseCode;
import board.common.ResponseMessage;
import board.dto.request.auth.LoginRequestDto;
import board.dto.request.auth.SignUpRequestDto;
import board.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.properties")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @BeforeEach
    void setUp() {

        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setUsername("testMember");
        signUpRequestDto.setPassword("testPassword");
        signUpRequestDto.setName("testName");
        signUpRequestDto.setEmail("test@test.com");

        memberService.signUp(signUpRequestDto);

    }

    @Test
    @DisplayName("회원가입 성공")
    void postSignUpSuccess() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setUsername("joinMember");
        signUpRequestDto.setPassword("joinPassword");
        signUpRequestDto.setName("joinName");
        signUpRequestDto.setEmail("join@join.com");

        //when
        mockMvc.perform(
                        post("/api/v1/auth/sign-up")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signUpRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andDo(print());
    }

    @Test
    @DisplayName("일부 필드 누락시 회원가입 시도")
    void postSignUpFailedSomeFieldAreMissing() throws Exception {
        //given
        SignUpRequestDto signUpRequestDtoNameFieldNull = new SignUpRequestDto();
        signUpRequestDtoNameFieldNull.setPassword("notUsername");
        signUpRequestDtoNameFieldNull.setName("notUsername");
        signUpRequestDtoNameFieldNull.setEmail("notUsername@test.com");

        //when
        mockMvc.perform(
                        post("/api/v1/auth/sign-up")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signUpRequestDtoNameFieldNull)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.VALIDATION_FAILED))
                .andExpect(jsonPath("$.message").value(ResponseMessage.VALIDATION_FAILED))
                .andDo(print());
    }

    @Test
    @DisplayName("아이디 중복 회원가입 시도")
    void signUpFailedExistUsername() throws Exception{
        //given
        SignUpRequestDto signUpRequestDtoExistUsername = new SignUpRequestDto();
        signUpRequestDtoExistUsername.setUsername("testMember");
        signUpRequestDtoExistUsername.setPassword("testPassword");
        signUpRequestDtoExistUsername.setName("testName");
        signUpRequestDtoExistUsername.setEmail("test@test.com");

        //when
        mockMvc.perform(
                        post("/api/v1/auth/sign-up")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signUpRequestDtoExistUsername)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ResponseCode.DUPLICATE_USERNAME))
                .andExpect(jsonPath("$.message").value(ResponseMessage.DUPLICATE_USERNAME))
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() throws Exception{
        //given
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("testMember");
        loginRequestDto.setPassword("testPassword");

        //when
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS))
                .andExpect(jsonPath("$.message").value(ResponseMessage.SUCCESS))
                .andDo(print());
    }
}