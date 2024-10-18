package organize.organizeJPA_study_1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import organize.organizeJPA_study_1.domain.Category;
import organize.organizeJPA_study_1.domain.enums.CategoryType;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("select c from Category c where c.categoryType in :types")
    List<Category> findByCategoryTypeIn(@Param("types") List<CategoryType> types);
}
