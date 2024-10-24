package organize.organizeJPA_study_1.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import organize.organizeJPA_study_1.domain.base.BaseInfo;
import organize.organizeJPA_study_1.domain.enums.CategoryType;
import organize.organizeJPA_study_1.domain.enums.ItemStatus;
import organize.organizeJPA_study_1.domain.enums.UseYn;
import organize.organizeJPA_study_1.domain.subtype.Album;
import organize.organizeJPA_study_1.domain.subtype.Book;
import organize.organizeJPA_study_1.domain.subtype.Movie;
import organize.organizeJPA_study_1.dto.request.ItemRequest;
import organize.organizeJPA_study_1.dto.request.UpdateItemRequest;
import organize.organizeJPA_study_1.dto.request.subtype.AlbumRequest;
import organize.organizeJPA_study_1.dto.request.subtype.BookRequest;
import organize.organizeJPA_study_1.dto.request.subtype.MovieRequest;
import organize.organizeJPA_study_1.exception.NotEnoughStockException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Item extends BaseInfo {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @Column(nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private UseYn useYn;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryItem> categoryItems = new ArrayList<>();

    public Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.itemStatus = ItemStatus.ON_SALE;
        this.useYn = UseYn.Y;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("재고가 부족합니다.", HttpStatus.BAD_REQUEST);
        }
        this.stockQuantity = restStock;
    }

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void toggleSaleStatus() {
        this.itemStatus = this.itemStatus.toggle();
    }

    public void addCategoryItem(CategoryItem ci) {
        this.categoryItems.add(ci);
        ci.addItem(this);
    }

    public void addTotalCategoryItems(List<CategoryItem> ci) {
        for (CategoryItem categoryItem : ci) {
            addCategoryItem(categoryItem);
        }
    }

    public void addMainCategoryItems(CategoryItem ci) {
        addCategoryItem(ci);
    }

    public void updateItem(ItemRequest itemCreateDto) {
        if (itemCreateDto.getName() != null && !itemCreateDto.getName().equals(this.name)) {
            this.name = itemCreateDto.getName();
        }
        if (itemCreateDto.getPrice() != this.price) {
            this.price = itemCreateDto.getPrice();
        }
        if (itemCreateDto.getStockQuantity() != this.stockQuantity) {
            this.stockQuantity = itemCreateDto.getStockQuantity();
        }

        updateItemDetails(itemCreateDto);
    }

    private void updateItemDetails(ItemRequest itemRequest) {
        if(itemRequest instanceof UpdateItemRequest updateItemRequest) {
            updateItemRequest.updateDetails(this);
        }
    }

    public void notUse() {
        this.useYn = UseYn.N;
    }

    public boolean isValidUpdate(Class<? extends ItemRequest> dtoClass) {
        if ((this instanceof Book && dtoClass != BookRequest.class) || (this instanceof Movie && dtoClass != MovieRequest.class) || (this instanceof Album && dtoClass != AlbumRequest.class)) {
            this.notUse();
            this.categoryItems.clear();
            return false;
        }
        return true;
    }

    public boolean areCategoryItemsAndTypesEqual(ItemRequest itemRequest) {
        boolean sizeCheck = this.categoryItems.size() == itemRequest.getCategoryTypes().size();
        boolean equalsCheck = sizeCheck && equalsCheck(itemRequest);

        if (!sizeCheck || !equalsCheck) {
            this.categoryItems.clear();
            return false;
        }

        return true;
    }

    private boolean equalsCheck(ItemRequest itemCreateDto) {
        List<CategoryItem> categoryItems = this.categoryItems;
        List<CategoryType> categoryTypesFromItem = categoryItems.stream()
                .map(categoryItem -> categoryItem.getCategory().getCategoryType()) // CategoryType 추출
                .toList();

        List<CategoryType> categoryTypesFromDto = itemCreateDto.getCategoryTypes();

        return new HashSet<>(categoryTypesFromItem).containsAll(categoryTypesFromDto);
    }

}
