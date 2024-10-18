package organize.organizeJPA_study_1.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import organize.organizeJPA_study_1.domain.Category;
import organize.organizeJPA_study_1.domain.enums.CategoryType;

@Getter @Setter
@NoArgsConstructor
public class CategoryDto {
    private CategoryType categoryType;

    public Category convert() {
        return Category.builder().categoryType(this.categoryType).build();
    }
}
