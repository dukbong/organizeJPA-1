package organize.organizeJPA_study_1.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import organize.organizeJPA_study_1.domain.enums.CategoryType;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class CategoryRequest {

    @NotNull(message = "메인 카테고리는 필수 사항입니다.")
    private CategoryType categoryType;
    private List<CategoryType> subCategoryType;

    public void setSubCategoryType(List<CategoryType> subCategoryType) {
        List<CategoryType> subCategoryTypeList = new ArrayList<>();
        for(CategoryType categoryType : subCategoryType) {
            if(categoryType.getLevel() == 1) {
                throw new IllegalArgumentException("상위 카테고리를 하위 카테고리로 지정할 수 없습니다.");
            }
            subCategoryTypeList.add(categoryType);
        }
        this.subCategoryType = subCategoryTypeList;
    }

}