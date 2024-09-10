package board.repository;

import board.entity.V2.Likes;

import java.util.Optional;

public interface LikesRepository {

    void save(Likes likes);

    Optional<Likes> findByMemberIdAndBoardId(Long memberId, Long boardId);

    void deleteByMemberIdAndBoardId(Long memberId, Long boardId);
}
