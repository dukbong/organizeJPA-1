package organize.organizeJPA_study_1.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import organize.organizeJPA_study_1.domain.base.BaseInfo;
import organize.organizeJPA_study_1.domain.enums.CategoryType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseInfo {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryType categoryType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> subcategories = new ArrayList<>();

    @Builder
    public Category(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public static void createHighCategory(CategoryType categoryType) {
        if (categoryType.getLevel() != 1) {
            throw new IllegalArgumentException("상위 카테고리만 지정 가능합니다.");
        }
    }

    // 하위 카테고리 추가 메서드
    public void addSubcategory(Category subcategory) {
        if (subcategory.getCategoryType().getLevel() != 2) {
            throw new IllegalArgumentException("상위 카테고리를 하위 카테고리로 지정할 수 없습니다.");
        }
        subcategories.add(subcategory);
        subcategory.setParent(this);
    }

    // 부모 카테고리 설정 메서드
    private void setParent(Category parent) {
        this.parent = parent;
    }

    public void validateExistingSubcategory(Long parentId) {
        if(this.id.equals(parentId)) {
            throw new IllegalArgumentException("이미 등록된 하위 카테고리입니다.");
        }
    }
}
