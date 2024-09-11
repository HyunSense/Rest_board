package board.entity.V2;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


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

    public void update(String title, String content) {

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
