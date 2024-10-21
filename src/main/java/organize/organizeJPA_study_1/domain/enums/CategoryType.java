package organize.organizeJPA_study_1.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryType {
    BOOK("책"),
    ALBUM("앨범"),
    MOVIE("영화");

    private String type;
}
