package organize.organizeJPA_study_1.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MovieCreateDto extends ItemCreateDto{

    @NotEmpty(message = "감독은 필수 입력 사항입니다.")
    private String director;
    private String actor;

}
