package organize.organizeJPA_study_1.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import organize.organizeJPA_study_1.domain.base.BaseInfo;
import organize.organizeJPA_study_1.domain.enums.CategoryType;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseInfo {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private CategoryType categoryType;

    @Builder
    public Category(CategoryType categoryType) {
        this.categoryType = categoryType;
    }
}
