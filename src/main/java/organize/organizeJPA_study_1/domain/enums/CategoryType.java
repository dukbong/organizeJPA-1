package organize.organizeJPA_study_1.domain.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum CategoryType {
    BOOK("책"),
    ALBUM("앨범"),
    MOVIE("영화");

    private String type;
}
