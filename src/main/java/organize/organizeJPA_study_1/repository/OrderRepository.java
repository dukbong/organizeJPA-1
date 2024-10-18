package organize.organizeJPA_study_1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import organize.organizeJPA_study_1.domain.Order;


public interface OrderRepository extends JpaRepository<Order, Integer> {
}
