package organize.organizeJPA_study_1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import organize.organizeJPA_study_1.domain.Item;
import organize.organizeJPA_study_1.domain.enums.ItemStatus;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {


    @Query("select i from Item i where i.itemStatus = :status")
    List<Item> findAllOn(ItemStatus status);
}
