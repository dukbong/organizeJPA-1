package organize.organizeJPA_study_1.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import organize.organizeJPA_study_1.domain.Category;
import organize.organizeJPA_study_1.domain.enums.CategoryType;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.categoryType in :types")
    List<Category> findByCategoryTypeIn(@Param("types") List<CategoryType> types);

    @Query("select c from Category c where c.parent.id = :parentId and c.categoryType in :types")
    List<Category> findByMainTypeAndCategoryTypeIn(List<CategoryType> types, Long parentId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Category c where c.id = :categoryId")
    Optional<Category> findByIdTest(@Param("categoryId")Long categoryId);

    @Query("select c from Category c where c.categoryType = :categoryType and c.parent is null")
    Optional<Category> findByMainCategoryTypeToEquals(@Param("categoryType") CategoryType categoryType);
}
