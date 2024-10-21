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
public class Movie extends Item {

    private String director;
    private String actor;

    @Builder
    public Movie(String name, int price, int stockQuantity, String director, String actor) {
        super(name, price, stockQuantity);
        this.director = director;
        this.actor = actor;
    }

    public void updateDirecotr(String director) {
        this.director = director;
    }

    public void updateActor(String actor) {
        this.actor = actor;
    }
}
