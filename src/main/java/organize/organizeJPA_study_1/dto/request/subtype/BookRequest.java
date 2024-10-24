package organize.organizeJPA_study_1.dto.request.subtype;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import organize.organizeJPA_study_1.domain.Item;
import organize.organizeJPA_study_1.domain.enums.CategoryType;
import organize.organizeJPA_study_1.domain.subtype.Book;
import organize.organizeJPA_study_1.dto.request.ItemRequest;
import organize.organizeJPA_study_1.dto.request.UpdateItemRequest;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
public class BookRequest extends ItemRequest implements UpdateItemRequest {

    @NotEmpty(message = "저자는 필수 입력 사항입니다.")
    private String author;
    private String isbn;

    public BookRequest(String name, int price, int stockQuantity, CategoryType mainCategory, List<CategoryType> categoryTypes, String author, String isbn) {
        super(name, price, stockQuantity, mainCategory, categoryTypes);
        this.author = author;
        this.isbn = isbn;
    }

    @Override
    public void updateDetails(Item item) {
        if (item instanceof Book book) { // 타입 체크 및 캐스팅
            if (this.author != null && !this.author.equals(book.getAuthor())) {
                book.updateAuthor(this.author);
            }
            if (this.isbn != null && !this.isbn.equals(book.getIsbn())) {
                book.updateIsbn(this.isbn);
            }
        }
    }
}
