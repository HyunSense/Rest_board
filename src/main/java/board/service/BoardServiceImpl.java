package board.service;

import board.dto.request.board.GetBoardAllRequestDto;
import board.dto.request.board.PatchBoardRequestDto;
import board.dto.request.board.PostBoardRequestDto;
import board.dto.response.*;
import board.dto.response.board.*;
import board.entity.Board;
import board.mapper.BoardMapper;
import board.mapper.resultset.GetBoardResultSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;

    @Override
    public ResponseEntity<? super PostBoardResponseDto> createBoard(Long memberId, PostBoardRequestDto dto) {

        try
        {
            String title = dto.getTitle();
            String content = dto.getContent();

            Board board = Board.builder()
                    .memberId(memberId)
                    .title(title)
                    .content(content)
                    .build();

            boardMapper.save(board);
            return PostBoardResponseDto.success();

        } catch (Exception e) {
            log.warn("createBoard exception = ", e);
            return ResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<? super GetBoardAllResponseDto> getAllBoards(GetBoardAllRequestDto dto) {

        List<GetBoardResultSet> boards = null;
        int page = dto.getPage();
        int limit = dto.getLimit();

        try {

            int offset = (page - 1) * limit;
            boards = boardMapper.getAllBoard(limit, offset);

            if (boards == null) {
                return GetBoardAllResponseDto.notExistBoard();
            }

        } catch (Exception e) {
            log.warn("getAllBoards exception = ", e);
            return ResponseDto.databaseError();
        }

        return GetBoardAllResponseDto.success(page, limit, boards);
    }

    @Override
    public ResponseEntity<? super GetBoardResponseDto> getBoardById(Long id) {

        GetBoardResultSet resultSet = null;

        try {

            resultSet = boardMapper.getBoardById(id);

            if (resultSet == null) {
                return GetBoardResponseDto.notExistBoard();
            }

        } catch (Exception e) {
            log.warn("getBoardById exception = ", e);
            return ResponseDto.databaseError();
        }

        return GetBoardResponseDto.success(resultSet);
    }

    @Override
    public ResponseEntity<? super PatchBoardResponseDto> updateBoard(PatchBoardRequestDto dto, Long memberId, Long id) {

        try {

            Board board = boardMapper.findById(id);

            if (board == null) {
                return PatchBoardResponseDto.notExistBoard();
            }

            Long boardMemberId = board.getMemberId();
            boolean isMemberId = boardMemberId.equals(memberId);

            if (!isMemberId) {
                return PatchBoardResponseDto.notPermission();
            }

            board.patchBoard(dto);
            boardMapper.updateBoard(board);

        } catch (Exception e) {
            log.warn("updateBoardById exception = ", e);
            return ResponseDto.databaseError();
        }

        return PatchBoardResponseDto.success();
    }

    @Override
    public ResponseEntity<? super DeleteBoardResponseDto> deleteBoard(Long memberId, Long id) {

        try {

            Board board = boardMapper.findById(id);

            if (board == null) {
                return DeleteBoardResponseDto.notExistBoard();
            }

            Long boardMemberId = board.getMemberId();
            boolean isMemberId = boardMemberId.equals(memberId);

            if (!isMemberId) {
                return DeleteBoardResponseDto.notPermission();
            }

            boardMapper.deleteById(id);

        } catch (Exception e) {
            log.warn("deleteBoardById exception = ", e);
            return ResponseDto.databaseError();
        }

        return DeleteBoardResponseDto.success();
    }
}
