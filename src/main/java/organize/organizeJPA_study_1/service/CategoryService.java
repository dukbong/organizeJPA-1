package organize.organizeJPA_study_1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import organize.organizeJPA_study_1.domain.Category;
import organize.organizeJPA_study_1.domain.Item;
import organize.organizeJPA_study_1.domain.enums.CategoryType;
import organize.organizeJPA_study_1.domain.enums.UseYn;
import organize.organizeJPA_study_1.dto.request.CategoryRequest;
import organize.organizeJPA_study_1.dto.response.CategoryResponse;
import organize.organizeJPA_study_1.mapper.Mapper;
import organize.organizeJPA_study_1.repository.CategoryRepository;
import organize.organizeJPA_study_1.repository.ItemRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final Mapper<CategoryRequest, Category, CategoryResponse> categoryMapper;

    @Transactional
    public Long saveHighCategory(CategoryRequest categoryDto) {
        validateHighCategory(categoryDto);
        Category existingCategories = findByMainCategoryTypeToEquals(categoryDto.getCategoryType());
        Category parentCategory = existingCategories == null ? saveNewCategory(categoryDto) : existingCategories;
        if (categoryDto.getSubCategoryType() != null) {
            addSubcategories(parentCategory, categoryDto);
        } else {
            if (existingCategories != null) throw new IllegalArgumentException("이미 등록된 상위 카테고리 입니다.");
        }

        return parentCategory.getId();
    }

    protected List<Category> findByCategoryTypeIn(List<CategoryType> categoryTypeList) {
        return categoryRepository.findByCategoryTypeIn(categoryTypeList);
    }

    protected Category findByMainCategoryTypeToEquals(CategoryType categoryType) {
        return categoryRepository.findByMainCategoryTypeToEquals(categoryType).orElse(null);
    }

    // 상위 카테고리 유효성 검증 메서드
    private void validateHighCategory(CategoryRequest categoryDto) {
        if (categoryDto.getCategoryType().getLevel() != 1) {
            throw new IllegalArgumentException("상위 카테고리만 지정 가능합니다.");
        }
    }

    // 새로운 카테고리 저장 메서드
    @Transactional
    protected Category saveNewCategory(CategoryRequest categoryDto) {
        Category newCategory = categoryMapper.toEntity(categoryDto);
        return categoryRepository.save(newCategory);
    }

    // 하위 카테고리 추가 메서드
    @Transactional
    protected void addSubcategories(Category parentCategory, CategoryRequest categoryDto) {
        List<Category> existingSubCategories = findByCategoryTypeIn(categoryDto.getSubCategoryType());
        if (!existingSubCategories.isEmpty()) {
            for (Category existingSubCategory : existingSubCategories) {
                parentCategory.validateExistingSubcategory(existingSubCategory.getParent().getId());
            }
        }

        for (CategoryType subCategoryType : categoryDto.getSubCategoryType()) {
            CategoryRequest subCategoryDto = createSubCategoryDto(subCategoryType);
            parentCategory.addSubcategory(categoryMapper.toEntity(subCategoryDto));
        }
    }

    // 하위 카테고리 DTO 생성 메서드
    private CategoryRequest createSubCategoryDto(CategoryType subCategoryType) {
        CategoryRequest subCategoryDto = new CategoryRequest();
        subCategoryDto.setCategoryType(subCategoryType);
        return subCategoryDto;
    }

    // 상위 카테고리 삭제
    // cascade에 의해 하위 카테고리까지 삭제
    @Transactional
    public void deleteHighCategory(Long highCategoryId) {
        Category category = findById(highCategoryId);

        if (category.getParent() != null) {
            throw new IllegalArgumentException("해당 카테고리는 상위 카테고리가 아닙니다.");
        }

        List<Item> allItemY = itemRepository.findAllWhereUseY(UseYn.Y);
        boolean exists = allItemY.stream()
                .anyMatch(item -> item.getCategoryItems().stream()
                        .anyMatch(categoryItem -> categoryItem.getCategory().getParent().getId().equals(category.getId())));
        if (exists) {
            throw new IllegalStateException("현재 사용 중인 카테고리는 삭제할 수 없습니다.");
        }

        categoryRepository.delete(category);
    }

    @Transactional
    protected Category findById(Long categoryId) {
        return categoryRepository.findByIdTest(categoryId).orElseThrow(
                () -> new IllegalArgumentException("카테고리를 찾을 수 없습니다.")
        );
    }

    // 하위 카테고리 삭제
    @Transactional
    public void deleteRowCategory(Long rowCategoryId) {
        Category subCategory = findById(rowCategoryId);

        Category parentCategory = subCategory.getParent();
        if (parentCategory == null) {
            throw new IllegalArgumentException("해당 카테고리는 하위 카테고리가 아닙니다.");
        }

        List<Item> allItem = itemRepository.findAllWhereUseY(UseYn.Y);
        boolean exists = allItem.stream()
                .anyMatch(item -> item.getCategoryItems().stream()
                        .anyMatch(categoryItem -> categoryItem.getCategory().getId().equals(subCategory.getId())));

        if (exists) {
            throw new IllegalStateException("현재 사용 중인 카테고리는 삭제할 수 없습니다.");
        }

        parentCategory.getSubcategories().remove(subCategory); // 부모 카테고리에서 해당 하위 카테고리 제거

        categoryRepository.delete(subCategory); // 하위 카테고리만 삭제
    }


}
