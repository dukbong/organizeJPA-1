package organize.organizeJPA_study_1.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import organize.organizeJPA_study_1.domain.base.BaseInfo;
import organize.organizeJPA_study_1.domain.enums.CategoryType;
import organize.organizeJPA_study_1.domain.enums.ItemStatus;
import organize.organizeJPA_study_1.domain.enums.UseYn;
import organize.organizeJPA_study_1.domain.itemtype.Album;
import organize.organizeJPA_study_1.domain.itemtype.Book;
import organize.organizeJPA_study_1.domain.itemtype.Movie;
import organize.organizeJPA_study_1.dto.*;
import organize.organizeJPA_study_1.exception.NotEnoughStockException;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Item extends BaseInfo {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @Column(nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private UseYn useYn;

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryItem> categoryItems = new ArrayList<>();

    protected Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.itemStatus = ItemStatus.ON_SALE;
        this.useYn = UseYn.Y;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("재고가 부족합니다.", HttpStatus.BAD_REQUEST);
        }
        this.stockQuantity = restStock;
    }

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void changeSale() {
        this.itemStatus = this.itemStatus.toggle();
    }

    public void addCategoryItem(CategoryItem ci) {
        this.categoryItems.add(ci);
        ci.addItem(this);
    }

    public void addCategoryItems(List<CategoryItem> ci) {
        for (CategoryItem categoryItem : ci) {
            addCategoryItem(categoryItem);
        }
    }

    public void updateItem(ItemCreateDto itemCreateDto) {
        if (itemCreateDto.getName() != null && !itemCreateDto.getName().equals(this.name)) {
            this.name = itemCreateDto.getName();
        }
        if (itemCreateDto.getPrice() != this.price) {
            this.price = itemCreateDto.getPrice();
        }
        if (itemCreateDto.getStockQuantity() != this.stockQuantity) {
            this.stockQuantity = itemCreateDto.getStockQuantity();
        }

        updateItemDetails(itemCreateDto);
    }

    private void updateItemDetails(ItemCreateDto itemCreateDto) {
        if (itemCreateDto instanceof BookCreateDto dto) {
            updateBookDetails(dto);
        } else if (itemCreateDto instanceof AlbumCreateDto dto) {
            updateAlbumDetails(dto);
        } else if (itemCreateDto instanceof MovieCreateDto dto) {
            updateMovieDetails(dto);
        }
    }

    private void updateBookDetails(BookCreateDto dto) {
        Book book = (Book) this;
        if (dto.getAuthor() != null && !dto.getAuthor().equals(book.getAuthor())) {
            book.updateAuthor(dto.getAuthor());
        }
        if (dto.getIsbn() != null && !dto.getIsbn().equals(book.getIsbn())) {
            book.updateIsbn(dto.getIsbn());
        }
    }

    private void updateAlbumDetails(AlbumCreateDto dto) {
        Album album = (Album) this;
        if (dto.getArtist() != null && !dto.getArtist().equals(((Album) this).getArtist())) {
            album.updateArtist(dto.getArtist());
        }
        if (dto.getEtc() != null && !dto.getEtc().equals(((Album) this).getEtc())) {
            album.updateEtc(dto.getEtc());
        }
    }

    private void updateMovieDetails(MovieCreateDto dto) {
        Movie movie = (Movie) this;
        if (dto.getDirector() != null && !dto.getDirector().equals(movie.getDirector())) {
            movie.updateDirector(dto.getDirector());
        }
        if (dto.getActor() != null && !dto.getActor().equals(movie.getActor())) {
            movie.updateActor(dto.getActor());
        }
    }

    public void notUse() {
        this.useYn = UseYn.N;
    }

    public boolean isValidUpdate(Class<? extends ItemCreateDto> dtoClass) {
        if (this instanceof Book && dtoClass != BookCreateDto.class) {
            return false;
        }
        if (this instanceof Movie && dtoClass != MovieCreateDto.class) {
            return false;
        }
        if (this instanceof Album && dtoClass != AlbumCreateDto.class) {
            return false;
        }
        return true;
    }
}
