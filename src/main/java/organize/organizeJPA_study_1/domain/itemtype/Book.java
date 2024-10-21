package organize.organizeJPA_study_1.domain.itemtype;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import organize.organizeJPA_study_1.domain.Item;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item {

    private String author;
    private String isbn;

    @Builder
    public Book(String name, int price, int stockQuantity, String author, String isbn) {
        super(name, price, stockQuantity);
        this.author = author;
        this.isbn = isbn;
    }

    public void updateAuthor(String author) {
        this.author = author;
    }

    public void updateIsbn(String isbn) {
        this.isbn = isbn;
    }
}
