package organize.organizeJPA_study_1.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import organize.organizeJPA_study_1.domain.enums.ItemStatus;

import java.util.List;

@Getter @Setter
public class BookResponse extends ItemResponse {
    private String author;
    private String isbn;

    @Builder
    public BookResponse(String name, int price, int stockQuantity, ItemStatus itemStatus, List<String> categorys, String author, String isbn) {
        super(name, price, stockQuantity, itemStatus, categorys);
        this.author = author;
        this.isbn = isbn;
    }
}
