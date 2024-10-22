package organize.organizeJPA_study_1.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryType {
    BOOK("책", 1),
    ALBUM("앨범", 1),
    MOVIE("영화", 1),

    DRAMA("드라마", 2),
    COMEDY("코미디", 2),
    HORROR("호러", 2);

    private String type;
    private int level;
}
