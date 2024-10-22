package organize.organizeJPA_study_1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import organize.organizeJPA_study_1.domain.Item;
import organize.organizeJPA_study_1.domain.enums.ItemStatus;
import organize.organizeJPA_study_1.domain.enums.UseYn;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i where i.useYn = :useYn")
    List<Item> findAllUseY(@Param("useYn") UseYn useYn);

    @Query("select i from Item i where i.id = :itemId and i.useYn = :useYn")
    Optional<Item> findByIdAndUseY(@Param("itemId") Long itemId, @Param("useYn") UseYn useYn);
}
