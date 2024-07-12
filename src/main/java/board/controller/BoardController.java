package board.controller;

import org.springframework.web.bind.annotation.*;

@RestController("/api/v1")
public class BoardController {

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
    @PostMapping("/post")
    public String create() {

        return "create";
    }

    // 글 수정
    @PatchMapping("/post/{id}")
    public String update(@PathVariable String id) {

        return "update" + id;
    }

    //글
    @DeleteMapping("/post/{id}")
    public String delete(@PathVariable String id) {

        return "delete" + id;
    }


}
