package organize.organizeJPA_study_1.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import organize.organizeJPA_study_1.domain.base.BaseInfo;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryItem extends BaseInfo {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private CategoryItem(Category category) {
        this.category = category;
    }

    //== 비즈니스 로직 ==//
    public static CategoryItem of(Category category) {
        return new CategoryItem(category);
    }

    public void addItem(Item item) {
        this.item = item;
    }
}
