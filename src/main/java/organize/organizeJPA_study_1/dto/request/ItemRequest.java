package organize.organizeJPA_study_1.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import organize.organizeJPA_study_1.domain.enums.CategoryType;
import organize.organizeJPA_study_1.dto.request.subtype.AlbumRequest;
import organize.organizeJPA_study_1.dto.request.subtype.BookRequest;
import organize.organizeJPA_study_1.dto.request.subtype.MovieRequest;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BookRequest.class, name = "book"),
        @JsonSubTypes.Type(value = AlbumRequest.class, name = "album"),
        @JsonSubTypes.Type(value = MovieRequest.class, name = "movie")
})
public class ItemRequest {

    @NotEmpty(message = "상품명은 필수 입력 사항입니다.")
    private String name;
    @NotNull(message = "상품 가격은 필수 입력 사항입니다.")
    @Min(value = 100, message = "상품 가격은 100원 부터 가능합니다.")
    private int price;
    @Min(value = 0, message = "재고 수량은 0개 부터 가능합니다.")
    private int stockQuantity;
    @NotNull(message = "메인 카테코리는 필수 입니다.")
    private CategoryType mainCategoryType;
    private List<CategoryType> categoryTypes;

    public ItemRequest(String name, int price, int stockQuantity, CategoryType mainCategoryType, List<CategoryType> categoryTypes) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.mainCategoryType = mainCategoryType;
        this.categoryTypes = categoryTypes;
    }
}
