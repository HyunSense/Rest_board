package board.controller;

import board.config.auth.PrincipalDetails;
import board.dto.response.ResponseDto;
import board.entity.Board;
import board.entity.Member;
import board.dto.request.auth.PostRequestDto;
import board.dto.response.PostResponseDto;
import board.jwt.JWTUtil;
import board.mapper.BoardMapper;
import board.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@RestController("/api/v1")
public class BoardController {

    private final JWTUtil jwtUtil;
    private final UserMapper userMapper;
    private final BoardMapper boardMapper;

    // 전체 글 목록
    @GetMapping("/posts")
    public String readAll() {

        return "posts";
    }

    // 글 조회
    @GetMapping("/post/{id}")
    public String read(@PathVariable String id) {

        return "read" + id;
    }

    // 글 작성

    //TODO: PECS 공식
    //TODO: 외부의 데이터를 생산(PRODUCER)한다면 <? extends ResponseDto>와 같은 패턴이 적합합니다.
    //TODO: 외부의 데이터를 소비(CONSUMER)한다면 <? super PostResponseDto>와 같은 패턴이 적합합니다.
    //TODO: https://inpa.tistory.com/entry/JAVA-%E2%98%95-%EC%A0%9C%EB%84%A4%EB%A6%AD-%EC%99%80%EC%9D%BC%EB%93%9C-%EC%B9%B4%EB%93%9C-extends-super-T-%EC%99%84%EB%B2%BD-%EC%9D%B4%ED%95%B4
    @PostMapping("/post")
    public ResponseEntity<? super PostResponseDto> create(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody @Valid PostRequestDto postRequestDto) {

//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//        String role = auth.getAuthority();

        String username = principalDetails.getUsername();
        log.info("username = {}", username);
        log.info("postDto = {}", postRequestDto);

        try {
            Member member = userMapper.findByUserName(username);
            if (member == null) {
                return PostResponseDto.notExistUser();
            }

            Long id = member.getId();
            String title = postRequestDto.getTitle();
            String content = postRequestDto.getContent();

            Board board = Board.builder()
                    .memberId(id)
                    .title(title)
                    .content(content)
                    .build();

            boardMapper.save(board);


        } catch (Exception e) {
            log.error("[POST] /post Request Error = ", e);
            return ResponseDto.databaseError();
        }

        return PostResponseDto.success();
    }

    // 글 수정
    @PatchMapping("/post/{id}")
    public String update(@PathVariable String id) {

        return "update" + id;
    }

    //글 삭제
    @DeleteMapping("/post/{id}")
    public String delete(@PathVariable String id) {

        return "delete" + id;
    }


}
