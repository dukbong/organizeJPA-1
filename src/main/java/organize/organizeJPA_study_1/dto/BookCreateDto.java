package organize.organizeJPA_study_1.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import organize.organizeJPA_study_1.domain.enums.CategoryType;

import java.util.List;

@Getter
@NoArgsConstructor
public class BookCreateDto extends ItemCreateDto{

    @NotEmpty(message = "저자는 필수 입력 사항입니다.")
    private String author;
    private String isbn;

    public BookCreateDto(String name, int price, int stockQuantity, List<CategoryType> categoryTypes, String author, String isbn) {
        super(name, price, stockQuantity, categoryTypes);
        this.author = author;
        this.isbn = isbn;
    }
}
