package board.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Member {

    private Long id;
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String role;
    private LocalDate createdDate;

}
