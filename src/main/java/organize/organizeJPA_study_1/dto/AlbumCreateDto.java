package organize.organizeJPA_study_1.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlbumCreateDto extends ItemCreateDto{

    @NotEmpty(message = "아티스트는 필수 입력 사항입니다.")
    private String artist;
    private String etc;
}
