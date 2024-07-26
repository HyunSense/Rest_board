package board.mapper;

import board.entity.Board;
import board.entity.Comment;
import board.entity.Likes;
import board.entity.Member;
import board.mapper.resultset.BoardResultSet;
import board.mapper.resultset.CommentListResultSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MybatisTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BoardMapperTest {

    @Autowired
    private BoardMapper boardMapper;
    @Autowired
    private UserMapper userMapper;

    @BeforeAll
    void setUp() {

        Member member = Member.builder()
                .username("testMember")
                .password("1234")
                .name("testMember")
                .email("testMember@naver.com")
                .role("ROLE_USER")
                .build();

        userMapper.save(member);
    }

    @Test
    @DisplayName("게시글 저장 확인")
    void saveBoardIsNotNull() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test Content")
                .build();

        //when
        boardMapper.saveBoard(board);

        //then
        List<Board> boardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board findBoard = boardList.get(0);
        Board findBoardById = boardMapper.findBoardById(findBoard.getId());

        assertThat(findBoardById).isNotNull();
        assertThat(findBoardById.getTitle()).isEqualTo("test Title");
        assertThat(findBoardById.getContent()).isEqualTo("test Content");
    }

    @Test
    @DisplayName("잘못된 Member Id로 게시글 확인")
    void saveBoardIsNull() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test Content")
                .build();
        boardMapper.saveBoard(board);

        //when
        Long nonExistBoardId = 2L;

        //then
        assertThat(board.getId()).isNotEqualTo(nonExistBoardId);
    }
    
    @Test
    @DisplayName("저장된 게시글 List 사이즈 확인")
    void saveBoardAllListSize() {
        //given
        Board board1 = Board.builder()
                .memberId(1L)
                .title("First test Title")
                .content("First test Content")
                .build();

        Board board2 = Board.builder()
                .memberId(1L)
                .title("Second test Title")
                .content("Second test Content")
                .build();

        //when
        boardMapper.saveBoard(board1);
        boardMapper.saveBoard(board2);
        List<Board> boardList = boardMapper.findAllByMemberId(1L);

        //then
        assertThat(boardList).hasSize(2);
    }

    @Test
    @DisplayName("Member Id로 저장된 게시글들 조회")
    void findBoardAllByMemberId() {
        //given
        Board board1 = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test Content")
                .build();

        Board board2 = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test Content")
                .build();

        //when
        boardMapper.saveBoard(board1);
        boardMapper.saveBoard(board2);
        List<Board> boardList = boardMapper.findAllByMemberId(1L);

        //then
        assertThat(boardList).isNotEmpty();
        Board saveBoard1 = boardList.get(0);
        assertThat(saveBoard1.getMemberId()).isEqualTo(1L);
        assertThat(saveBoard1.getTitle()).isEqualTo("test Title");
        assertThat(saveBoard1.getContent()).isEqualTo("test Content");

        Board saveBoard2 = boardList.get(1);
        assertThat(saveBoard2.getMemberId()).isEqualTo(1L);
        assertThat(saveBoard2.getTitle()).isEqualTo("test Title");
        assertThat(saveBoard2.getContent()).isEqualTo("test Content");

    }

    @Test
    @DisplayName("저장된 게시글 username 확인")
    void getBoardByIdExistMemberUsername() {

        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test Content")
                .build();
        boardMapper.saveBoard(board);

        //when
        BoardResultSet boardResultSet = boardMapper.getBoardById(1L);

        //then
        assertThat(boardResultSet).isNotNull();
        assertThat(boardResultSet.getAuthor()).isEqualTo("testMember");
        assertThat(boardResultSet.getTitle()).isEqualTo("test Title");
    }

    @Test
    @DisplayName("저장된 게시글 존재하지 않는 username 확인")
    void getBoardByIdNonExistUsername() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test Content")
                .build();
        boardMapper.saveBoard(board);

        //when
        List<Board> boardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board findBoard = boardList.get(0);
        BoardResultSet boardResultSet = boardMapper.getBoardById(findBoard.getId());

        //then
        assertThat(boardResultSet).isNotNull();
        assertThat(boardResultSet.getAuthor()).isNotEqualTo("nonExistMember");
    }

    @Test
    @DisplayName("username 포함된 저장된 게시글 List 사이즈 확인")
    void getAllBoardListSizeExistUsername() {
        //given
        Board board1 = Board.builder()
                .memberId(1L)
                .title("First test Title")
                .content("First test Content")
                .build();

        Board board2 = Board.builder()
                .memberId(1L)
                .title("Second test Title")
                .content("Second test Content")
                .build();

        boardMapper.saveBoard(board1);
        boardMapper.saveBoard(board2);

        //when
        List<BoardResultSet> boardList = boardMapper.getAllBoard(2, 0);

        //then
        assertThat(boardList).hasSize(2);
    }

    @Test
    @DisplayName("존재하는 Member username 포함된 게시글 List 가져오기")
    void getAllBoardExistUsername() {
        //given
        Board board1 = Board.builder()
                .memberId(1L)
                .title("First test Title")
                .content("First test Content")
                .build();

        Board board2 = Board.builder()
                .memberId(1L)
                .title("Second test Title")
                .content("Second test Content")
                .build();

        boardMapper.saveBoard(board1);
        boardMapper.saveBoard(board2);

        //when
        List<BoardResultSet> boardList = boardMapper.getAllBoard(2, 0);

        //then
        assertThat(boardList.get(0)).isNotNull();
        assertThat(boardList.get(1)).isNotNull();
        assertThatThrownBy(() -> boardList.get(2)).isInstanceOf(IndexOutOfBoundsException.class);

        BoardResultSet boardResultSet1 = boardList.get(0);
        assertThat(boardResultSet1.getAuthor()).isEqualTo("testMember");

        BoardResultSet boardResultSet2 = boardList.get(1);
        assertThat(boardResultSet2.getAuthor()).isEqualTo("testMember");
    }

    @Test
    @DisplayName("게시글 조회수 증가 확인")
    void increaseViewCountBoard() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test Content")
                .build();

        boardMapper.saveBoard(board);
        List<Board> boardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board findBoard = boardList.get(0);
        findBoard.increaseViewCount();
        findBoard.increaseViewCount();

        //when
        boardMapper.updateViewCountBoard(findBoard);

        //then
        List<Board> updateBoard = boardMapper.findAllByMemberId(board.getMemberId());
        assertThat(updateBoard.get(0).getViewCount()).isEqualTo(2L);
    }

    @Test
    @DisplayName("게시글 댓글 수 증가 확인")
    void increaseCommentCountBoard() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test Content")
                .build();

        boardMapper.saveBoard(board);
        List<Board> boardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board findBoard = boardList.get(0);
        findBoard.increaseComment();
        findBoard.increaseComment();
        findBoard.increaseComment();

        //when
        boardMapper.updateCommentCountBoard(findBoard);

        //then
        List<Board> updateBoard = boardMapper.findAllByMemberId(board.getMemberId());
        assertThat(updateBoard.get(0).getCommentCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("게시글 댓글 수 감소 확인")
    void decreaseCommentCountBoard() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test Content")
                .build();

        boardMapper.saveBoard(board);
        List<Board> givenBoardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board givenBoard = givenBoardList.get(0);
        givenBoard.increaseComment();
        boardMapper.updateCommentCountBoard(givenBoard);

        //when
        List<Board> whenBoardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board whenBoard = whenBoardList.get(0);
        whenBoard.decreaseComment();
        boardMapper.updateCommentCountBoard(whenBoard);

        //then
        List<Board> updateBoard = boardMapper.findAllByMemberId(board.getMemberId());
        assertThat(updateBoard.get(0).getCommentCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("좋아요 증가 수 확인")
    void increaseLikesCountBoard() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test Content")
                .build();

        boardMapper.saveBoard(board);
        List<Board> givenBoardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board givenBoard = givenBoardList.get(0);
        givenBoard.increaseLikes();
        givenBoard.increaseLikes();
        givenBoard.increaseLikes();

        //when
        boardMapper.updateLikesCountBoard(givenBoard);

        //then
        List<Board> updateBoard = boardMapper.findAllByMemberId(board.getMemberId());
        assertThat(updateBoard.get(0).getLikesCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("좋아요 감소 수 확인")
    void decreaseLikesCountBoard() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test Content")
                .build();

        boardMapper.saveBoard(board);
        List<Board> givenBoardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board givenBoard = givenBoardList.get(0);
        givenBoard.increaseLikes();
        givenBoard.increaseLikes();
        givenBoard.increaseLikes();

        //when
        List<Board> whenBoardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board whenBoard = whenBoardList.get(0);
        whenBoard.decreaseLikes();
        boardMapper.updateLikesCountBoard(whenBoard);

        //then
        List<Board> updateBoard = boardMapper.findAllByMemberId(board.getMemberId());
        assertThat(updateBoard.get(0).getLikesCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글 수정 확인")
    void updateBoard() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test Content")
                .build();

        boardMapper.saveBoard(board);
        List<Board> boardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board givenBoard = boardList.get(0);
        Long givenId = givenBoard.getId();

        Board updateBoard = new Board(
                givenId, board.getMemberId(),
                "update test Title", "update test Content",
                0, 0, 0,
                LocalDateTime.now(), LocalDateTime.now(),
                0
        );

        //when

        boardMapper.updateBoard(updateBoard);

        //then
        List<Board> thenList = boardMapper.findAllByMemberId(board.getMemberId());
        Board thenBoard = thenList.get(0);
        assertThat(thenBoard).isNotNull();
        assertThat(thenBoard.getId()).isEqualTo(givenBoard.getId());
        assertThat(thenBoard.getTitle()).isEqualTo("update test Title");
        assertThat(thenBoard.getContent()).isEqualTo("update test Content");
    }
    
    @Test
    @DisplayName("없는 게시글 수정 확인")
    void updateNonExistBoard() {
        //given
        Board nonExistingBoard = new Board(
                999L, 1L,
                "Non-existing Title", "Non-existing Content",
                0, 0, 0,
                LocalDateTime.now(), LocalDateTime.now(),
                0
        );

        //when
        boardMapper.updateBoard(nonExistingBoard);

        //then
        List<Board> updateBoardList = boardMapper.findAllByMemberId(nonExistingBoard.getMemberId());
        assertThat(updateBoardList).isEmpty();
    }

    @Test
    @DisplayName("게시글 삭제")
    void deleteBoardById() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test Content")
                .build();

        boardMapper.saveBoard(board);
        List<Board> boardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board givenBoard = boardList.get(0);

        //when
        boardMapper.deleteBoardById(givenBoard.getId());

        //then
        Board deleteBoard = boardMapper.findBoardById(givenBoard.getId());
        assertThat(deleteBoard).isNull();
    }

    @Test
    @DisplayName("존재하지 않는 게시글 삭제")
    void deleteNonExistBoard() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test Content")
                .build();

        boardMapper.saveBoard(board);
        List<Board> boardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board givenBoard = boardList.get(0);

        int size = boardList.size();

        //when
        boardMapper.deleteBoardById(999L);

        //then
        List<Board> updatedBoardList = boardMapper.findAllByMemberId(board.getMemberId());
        int updatedSize = updatedBoardList.size();

        assertThat(updatedSize).isEqualTo(size);
    }

    @DisplayName("글 검색(type과 keyword로 검색 후 게시글 존재할때)")
    @ParameterizedTest
    @CsvSource({
            "title, Title, 4",
            "title, First, 1",
            "title, content, 0",
            "title, First Title, 1",
            "title, d, 2",
            "title, ir, 2",
            "username, testMember, 4",
            "content, content, 4"
    })
    void findBoardByTitle(String type, String keyword, int expectedSize) {
        //given
        Board board1 = Board.builder()
                .memberId(1L)
                .title("First Title")
                .content("First content")
                .build();

        Board board2 = Board.builder()
                .memberId(1L)
                .title("Second Title")
                .content("Second content")
                .build();

        Board board3 = Board.builder()
                .memberId(1L)
                .title("Third Title")
                .content("Third content")
                .build();

        Board board4 = Board.builder()
                .memberId(1L)
                .title("Forth Title")
                .content("Forth content")
                .build();

        boardMapper.saveBoard(board1);
        boardMapper.saveBoard(board2);
        boardMapper.saveBoard(board3);
        boardMapper.saveBoard(board4);

        //then
        List<BoardResultSet> resultList = boardMapper.findBoardByTypeAndKeyword(type, keyword);

        //when
        assertThat(resultList).hasSize(expectedSize);

    }

    @ParameterizedTest
    @DisplayName("글 검색(type과 keyword로 검색 후 게시글 존재하지 않을때)")
    @CsvSource({
            "title, NonExist, 0",
            "username, NonExist, 0",
            "content, NonExist, 0"
    })
    void findBoardByTypeAndNonExistKeyword(String type, String keyword, int expectedSize) {
        // given
        Board board1 = Board.builder()
                .memberId(1L)
                .title("First Title")
                .content("First content")
                .build();

        Board board2 = Board.builder()
                .memberId(1L)
                .title("Second Title")
                .content("Second content")
                .build();

        Board board3 = Board.builder()
                .memberId(1L)
                .title("Third Title")
                .content("Third content")
                .build();

        Board board4 = Board.builder()
                .memberId(1L)
                .title("Forth Title")
                .content("Forth content")
                .build();

        boardMapper.saveBoard(board1);
        boardMapper.saveBoard(board2);
        boardMapper.saveBoard(board3);
        boardMapper.saveBoard(board4);

        // when
        List<BoardResultSet> results = boardMapper.findBoardByTypeAndKeyword(type, keyword);

        // then
        assertThat(results).hasSize(expectedSize);
    }


    @DisplayName("글 검색(잘못된 타입으로 검색할 때)")
    @ParameterizedTest
    @CsvSource({
            "wrongType, title"
    })
    void findBoardByNonExistTypeAndKeyword(String type, String keyword) {
        //given
        Board board1 = Board.builder()
                .memberId(1L)
                .title("First Title")
                .content("First content")
                .build();

        Board board2 = Board.builder()
                .memberId(1L)
                .title("Second Title")
                .content("Second content")
                .build();

        boardMapper.saveBoard(board1);
        boardMapper.saveBoard(board2);

        //when
        List<BoardResultSet> resultList = boardMapper.findBoardByTypeAndKeyword(type, keyword);

        //then
        assertThat(resultList).isEmpty();
    }

    @Test
    @DisplayName("댓글 저장 및 조회")
    void findCommentBoard() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test content")
                .build();

        boardMapper.saveBoard(board);
        List<Board> boardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board findBoard = boardList.get(0);

        Comment comment = Comment.builder()
                .memberId(1L)
                .boardId(findBoard.getId())
                .content("test comment")
                .build();


        //when
        boardMapper.saveCommentBoard(comment);
        List<CommentListResultSet> commentList = boardMapper.findAllCommentByBoardId(findBoard.getId());

        //then
        assertThat(commentList).hasSize(1);

        CommentListResultSet findComment = commentList.get(0);
        assertThat(findComment.getContent()).isEqualTo("test comment");
    }

    @Test
    @DisplayName("존재하지않는 게시글에서 댓글 조회")
    void findCommentNonExistBoard() {
        //given
        Long nonExistBoardId = 9999L;

        //when
        List<CommentListResultSet> resultList = boardMapper.findAllCommentByBoardId(nonExistBoardId);

        //then
        assertThat(resultList).isEmpty();
    }

    @Test
    @DisplayName("게시글의 댓글 개수 조회")
    void countComment() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test content")
                .build();

        boardMapper.saveBoard(board);
        List<Board> boardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board findBoard = boardList.get(0);

        Comment comment1 = Comment.builder()
                .memberId(1L)
                .boardId(findBoard.getId())
                .content("First comment")
                .build();

        Comment comment2 = Comment.builder()
                .memberId(1L)
                .boardId(findBoard.getId())
                .content("Second comment")
                .build();

        Comment comment3 = Comment.builder()
                .memberId(1L)
                .boardId(findBoard.getId())
                .content("Third comment")
                .build();

        boardMapper.saveCommentBoard(comment1);
        boardMapper.saveCommentBoard(comment2);
        boardMapper.saveCommentBoard(comment3);

        //when
        int count = boardMapper.countCommentByBoardId(findBoard.getId());

        //then
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("잘못된 게시물 댓글 개수 확인")
    void countCommentNonExistBoard() {
        //given
        Long wrongBoardId = 2222L;

        //when
        int count = boardMapper.countCommentByBoardId(wrongBoardId);

        //then
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글에있는 특정 댓글 삭제 확인")
    void deleteCommentBoardById() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test content")
                .build();

        boardMapper.saveBoard(board);
        List<Board> boardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board findBoard = boardList.get(0);

        Comment comment = Comment.builder()
                .memberId(1L)
                .boardId(findBoard.getId())
                .content("First comment")
                .build();

        boardMapper.saveCommentBoard(comment);
        List<CommentListResultSet> commentList = boardMapper.findAllCommentByBoardId(findBoard.getId());
        CommentListResultSet findComment = commentList.get(0);

        //when
        boardMapper.deleteCommentBoardById(findBoard.getId(), findComment.getId());
        int count = boardMapper.countCommentByBoardId(findBoard.getId());

        //then
        assertThat(count).isEqualTo(0);

    }

    @Test
    @DisplayName("게시글에있는 전체 댓글 삭제 확인")
    void deleteCommentAllBoardByBoardId() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test content")
                .build();

        boardMapper.saveBoard(board);
        List<Board> boardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board findBoard = boardList.get(0);

        Comment comment1 = Comment.builder()
                .memberId(1L)
                .boardId(findBoard.getId())
                .content("First comment")
                .build();

        Comment comment2 = Comment.builder()
                .memberId(1L)
                .boardId(findBoard.getId())
                .content("First comment")
                .build();

        Comment comment3 = Comment.builder()
                .memberId(1L)
                .boardId(findBoard.getId())
                .content("First comment")
                .build();

        boardMapper.saveCommentBoard(comment1);
        boardMapper.saveCommentBoard(comment2);
        boardMapper.saveCommentBoard(comment3);

        //when
        boardMapper.deleteCommentBoardAllByBoardId(findBoard.getId());
        List<CommentListResultSet> commentList = boardMapper.findAllCommentByBoardId(findBoard.getId());

        //then
        assertThat(commentList).isEmpty();
    }

    @Test
    @DisplayName("게시글 좋아요 저장 및 조회")
    void saveLikesBoard() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test content")
                .build();

        boardMapper.saveBoard(board);
        boardMapper.saveBoard(board);
        List<Board> boardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board findBoard = boardList.get(0);

        Likes like = Likes.builder()
                .memberId(1L)
                .boardId(findBoard.getId())
                .build();

        //when
        boardMapper.saveLikesBoard(like);

        //then
        Likes likes = boardMapper.findLikesByMemberIdAndBoardId(findBoard.getMemberId(), findBoard.getId());
        assertThat(likes).isNotNull();
        assertThat(likes.getBoardId()).isEqualTo(findBoard.getId());
        assertThat(likes.getMemberId()).isEqualTo(findBoard.getMemberId());
    }

    @Test
    @DisplayName("게시글 좋아요 취소(삭제)")
    void deleteLikesBoard() {
        //given
        Board board = Board.builder()
                .memberId(1L)
                .title("test Title")
                .content("test content")
                .build();

        boardMapper.saveBoard(board);
        boardMapper.saveBoard(board);
        List<Board> boardList = boardMapper.findAllByMemberId(board.getMemberId());
        Board findBoard = boardList.get(0);

        Likes like = Likes.builder()
                .memberId(1L)
                .boardId(findBoard.getId())
                .build();
        boardMapper.saveLikesBoard(like);

        //when
        boardMapper.deleteLikesBoard(like);

        //then
        Likes likes = boardMapper.findLikesByMemberIdAndBoardId(findBoard.getMemberId(), findBoard.getId());
        assertThat(likes).isNull();
    }
}
