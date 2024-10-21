package organize.organizeJPA_study_1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import organize.organizeJPA_study_1.domain.Category;
import organize.organizeJPA_study_1.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Long categoryHighSave() {
        Category category = categoryRepository.save("추가할 상위 카테고리");
        return category.getId();
    }

    public void categoryLowSave() {
        Category findHigh = categoryRepository.findById(1).orElseThrow(() -> new IllegalArgumentException("상위 카테고리를 찾을 수 없습니다."));
        findHigh.addSubcategory("추가할 하위 카테고리");
    }

}
