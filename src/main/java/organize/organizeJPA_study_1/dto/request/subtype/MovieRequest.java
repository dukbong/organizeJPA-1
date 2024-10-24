package organize.organizeJPA_study_1.dto.request.subtype;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import organize.organizeJPA_study_1.domain.Item;
import organize.organizeJPA_study_1.domain.enums.CategoryType;
import organize.organizeJPA_study_1.domain.subtype.Book;
import organize.organizeJPA_study_1.domain.subtype.Movie;
import organize.organizeJPA_study_1.dto.request.ItemRequest;
import organize.organizeJPA_study_1.dto.request.UpdateItemRequest;

import java.util.List;

@Getter
@NoArgsConstructor
public class MovieRequest extends ItemRequest implements UpdateItemRequest {

    @NotEmpty(message = "감독은 필수 입력 사항입니다.")
    private String director;
    private String actor;

    public MovieRequest(String name, int price, int stockQuantity, CategoryType mainCategory, List<CategoryType> categoryTypes, String director, String actor) {
        super(name, price, stockQuantity, mainCategory, categoryTypes);
        this.director = director;
        this.actor = actor;
    }

    @Override
    public void updateDetails(Item item) {
        if (item instanceof Movie movie) { // 타입 체크 및 캐스팅
            if (this.director != null && !this.director.equals(movie.getDirector())) {
                movie.updateDirector(this.director);
            }
            if (this.actor != null && !this.actor.equals(movie.getActor())) {
                movie.updateActor(this.actor);
            }
        }
    }

}
