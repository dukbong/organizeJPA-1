package organize.organizeJPA_study_1.dto;

import lombok.Getter;
import lombok.Setter;
import organize.organizeJPA_study_1.domain.enums.ItemStatus;

import java.util.List;

@Getter @Setter
public class ItemResponse {

    private String name;
    private int price;
    private int stockQuantity;
    private ItemStatus itemStatus;
    private String mainCategory;
    private List<String> categoryList;

    public ItemResponse(String name, int price, int stockQuantity, ItemStatus itemStatus, String mainCategory, List<String> categoryList) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.itemStatus = itemStatus;
        this.mainCategory = mainCategory;
        this.categoryList = categoryList;
    }
}
