package organize.organizeJPA_study_1.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import organize.organizeJPA_study_1.domain.enums.CategoryType;

import java.util.List;

@Getter
@NoArgsConstructor
public class AlbumCreateDto extends ItemCreateDto {

    @NotEmpty(message = "아티스트는 필수 입력 사항입니다.")
    private String artist;
    private String etc;

    public AlbumCreateDto(String name, int price, int stockQuantity, CategoryType mainCategory, List<CategoryType> categoryTypes, String artist, String etc) {
        super(name, price, stockQuantity, mainCategory, categoryTypes);
        this.artist = artist;
        this.etc = etc;
    }
}
