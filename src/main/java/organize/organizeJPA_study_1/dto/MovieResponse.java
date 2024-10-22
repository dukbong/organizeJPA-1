package organize.organizeJPA_study_1.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import organize.organizeJPA_study_1.domain.enums.ItemStatus;

import java.util.List;

@Getter @Setter
public class MovieResponse extends ItemResponse{
    private String director;
    private String actor;

    @Builder
    public MovieResponse(String name, int price, int stockQuantity, ItemStatus itemStatus, String mainCategory, List<String> categoryList, String director, String actor) {
        super(name, price, stockQuantity, itemStatus, mainCategory, categoryList);
        this.director = director;
        this.actor = actor;
    }
}
