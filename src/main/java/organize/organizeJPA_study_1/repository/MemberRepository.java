package organize.organizeJPA_study_1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import organize.organizeJPA_study_1.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);
}
