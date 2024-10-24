package organize.organizeJPA_study_1.dto.request.subtype;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;
import organize.organizeJPA_study_1.domain.Item;
import organize.organizeJPA_study_1.domain.enums.CategoryType;
import organize.organizeJPA_study_1.domain.subtype.Album;
import organize.organizeJPA_study_1.domain.subtype.Book;
import organize.organizeJPA_study_1.dto.request.ItemRequest;
import organize.organizeJPA_study_1.dto.request.UpdateItemRequest;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
public class AlbumRequest extends ItemRequest implements UpdateItemRequest {

    @NotEmpty(message = "아티스트는 필수 입력 사항입니다.")
    private String artist;
    private String etc;

    public AlbumRequest(String name, int price, int stockQuantity, CategoryType mainCategory, List<CategoryType> categoryTypes, String artist, String etc) {
        super(name, price, stockQuantity, mainCategory, categoryTypes);
        this.artist = artist;
        this.etc = etc;
    }

    @Override
    public void updateDetails(Item item) {
        if (item instanceof Album album) { // 타입 체크 및 캐스팅
            if (this.artist != null && !this.artist.equals(album.getArtist())) {
                album.updateArtist(this.artist);
            }
            if (this.etc != null && !this.etc.equals(album.getEtc())) {
                album.updateEtc(this.etc);
            }
        }
    }
}
