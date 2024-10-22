package organize.organizeJPA_study_1.mapper;

import org.springframework.stereotype.Component;
import organize.organizeJPA_study_1.domain.Category;
import organize.organizeJPA_study_1.domain.Member;
import organize.organizeJPA_study_1.domain.embed.Address;
import organize.organizeJPA_study_1.domain.enums.CategoryType;
import organize.organizeJPA_study_1.dto.BookResponse;
import organize.organizeJPA_study_1.dto.CategoryDto;
import organize.organizeJPA_study_1.dto.CategoryResponse;
import organize.organizeJPA_study_1.dto.MemberJoinDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapper implements Mapper<CategoryDto, Category, CategoryResponse> {

    @Override
    public Category toEntity(CategoryDto dto) {
        return Category.builder().categoryType(dto.getCategoryType()).build();
    }

    @Override
    public CategoryDto toDto(Category entity) {
        return null;
    }

    @Override
    public List<CategoryResponse> toResponseListDto(List<Category> entities) {
        return List.of();
    }

}