package organize.organizeJPA_study_1.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import organize.organizeJPA_study_1.domain.enums.CategoryType;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class CategoryDto {

    private CategoryType categoryType;
    private List<CategoryType> subCategoryType;

    //== 바인딩 시 제약 조건 ==//
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