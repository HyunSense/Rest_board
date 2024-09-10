package board.entity.V2;

import board.dto.response.board.V2.PostDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String content;

    private long viewCount;

    private int commentCount;

    private int likesCount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean isDeleted;

//    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
//    private List<Comment> comments = new ArrayList<>();
//
//    public void addComment(Comment comment) {
//
//        comments.add(comment);
//        comment.setBoard(this);
//
//    }

    public void update(String title, String content) {

//        if (StringUtils.hasText(title)) {
//            this.title = title;
//        }
//        if (StringUtils.hasText(content)) {
//            this.content = content;
//        }

        this.title = title;
        this.content = content;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void increaseComment() {
        this.commentCount++;
    }

    public void decreaseComment() {
        this.commentCount--;
    }

    public void increaseLikes(){
        this.likesCount++;
    }

    public void decreaseLikes() {
        this.likesCount--;
    }
}
