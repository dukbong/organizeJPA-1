package organize.organizeJPA_study_1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import organize.organizeJPA_study_1.domain.Category;
import organize.organizeJPA_study_1.domain.enums.CategoryType;
import organize.organizeJPA_study_1.dto.CategoryDto;
import organize.organizeJPA_study_1.dto.CategoryResponse;
import organize.organizeJPA_study_1.mapper.Mapper;
import organize.organizeJPA_study_1.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final Mapper<CategoryDto, Category, CategoryResponse> categoryMapper;

    public Long saveHighCategory(CategoryDto categoryDto) {
        validateHighCategory(categoryDto);

        List<Category> existingCategories = categoryRepository.findByCategoryTypeIn(List.of(categoryDto.getCategoryType()));
        Category parentCategory = existingCategories.isEmpty() ? saveNewCategory(categoryDto) : existingCategories.get(0);

        if (categoryDto.getSubCategoryType() != null) {
            addSubcategories(parentCategory, categoryDto);
        } else {
            if(!existingCategories.isEmpty()) throw new IllegalArgumentException("이미 등록된 상위 카테고리 입니다.");
        }

        return parentCategory.getId();
    }

    // 상위 카테고리 유효성 검증 메서드
    private void validateHighCategory(CategoryDto categoryDto) {
        if (categoryDto.getCategoryType().getLevel() != 1) {
            throw new IllegalArgumentException("상위 카테고리만 지정 가능합니다.");
        }
    }

    // 새로운 카테고리 저장 메서드
    private Category saveNewCategory(CategoryDto categoryDto) {
        Category newCategory = categoryMapper.toEntity(categoryDto);
        return categoryRepository.save(newCategory);
    }

    // 하위 카테고리 추가 메서드
    private void addSubcategories(Category parentCategory, CategoryDto categoryDto) {
        for (CategoryType subCategoryType : categoryDto.getSubCategoryType()) {
            CategoryDto subCategoryDto = createSubCategoryDto(subCategoryType);

            List<Category> existingSubCategories = categoryRepository.findByCategoryTypeIn(List.of(subCategoryType));
            if (!existingSubCategories.isEmpty()) {
                for(Category existingSubCategory : existingSubCategories) {
                    Long existingSubCategoryId = existingSubCategory.getParent().getId();
                    parentCategory.validateExistingSubcategory(existingSubCategoryId);
                }
            }

            Category subCategory = categoryMapper.toEntity(subCategoryDto);
            parentCategory.addSubcategory(subCategory);
        }
    }

    // 하위 카테고리 DTO 생성 메서드
    private CategoryDto createSubCategoryDto(CategoryType subCategoryType) {
        CategoryDto subCategoryDto = new CategoryDto();
        subCategoryDto.setCategoryType(subCategoryType);
        return subCategoryDto;
    }
}
