package organize.organizeJPA_study_1.dto.response.subtype;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import organize.organizeJPA_study_1.domain.enums.ItemStatus;
import organize.organizeJPA_study_1.dto.response.ItemResponse;

import java.util.List;

@Getter @Setter
public class AlbumResponse extends ItemResponse {
    private String artist;
    private String etc;

    @Builder
    public AlbumResponse(String name, int price, int stockQuantity, ItemStatus itemStatus, String mainCategory, List<String> categoryList, String artist, String etc) {
        super(name, price, stockQuantity, itemStatus, mainCategory, categoryList);
        this.artist = artist;
        this.etc = etc;
    }
}
