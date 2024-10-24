package organize.organizeJPA_study_1.mapper;

import org.springframework.stereotype.Component;
import organize.organizeJPA_study_1.domain.Category;
import organize.organizeJPA_study_1.dto.request.CategoryRequest;
import organize.organizeJPA_study_1.dto.response.CategoryResponse;

import java.util.List;

@Component
public class CategoryMapper implements Mapper<CategoryRequest, Category, CategoryResponse> {

    @Override
    public Category toEntity(CategoryRequest dto) {
        return Category.builder().categoryType(dto.getCategoryType()).build();
    }

    @Override
    public CategoryRequest toDto(Category entity) {
        return null;
    }

    @Override
    public List<CategoryResponse> toResponseListDto(List<Category> entities) {
        return List.of();
    }

}