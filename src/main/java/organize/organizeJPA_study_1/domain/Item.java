package organize.organizeJPA_study_1.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import organize.organizeJPA_study_1.domain.base.BaseInfo;
import organize.organizeJPA_study_1.domain.enums.ItemStatus;
import organize.organizeJPA_study_1.exception.NotEnoughStockException;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Item extends BaseInfo {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @OneToMany(mappedBy = "item")
    private List<CategoryItem> categoryItems = new ArrayList<>();

    protected Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.itemStatus = ItemStatus.ON_SALE;
    }

    //==비즈니스 로직==//
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) {
            throw new NotEnoughStockException("재고가 부족합니다.", HttpStatus.BAD_REQUEST);
        }
        this.stockQuantity = restStock;
    }

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void offSale() {
        this.itemStatus = ItemStatus.OFF_SALE;
    }

    public void addCategoryItem(CategoryItem ci) {
        this.categoryItems.add(ci);
        ci.addItem(this);
    }
}
