package organize.organizeJPA_study_1.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import organize.organizeJPA_study_1.domain.enums.CategoryType;

import java.util.List;

@Getter
@NoArgsConstructor
public class MovieCreateDto extends ItemCreateDto{

    @NotEmpty(message = "감독은 필수 입력 사항입니다.")
    private String director;
    private String actor;

    public MovieCreateDto(String name, int price, int stockQuantity, CategoryType mainCategory, List<CategoryType> categoryTypes, String director, String actor) {
        super(name, price, stockQuantity, mainCategory, categoryTypes);
        this.director = director;
        this.actor = actor;
    }

}
