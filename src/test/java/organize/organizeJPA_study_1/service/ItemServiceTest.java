package organize.organizeJPA_study_1.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import organize.organizeJPA_study_1.domain.Category;
import organize.organizeJPA_study_1.domain.Item;
import organize.organizeJPA_study_1.domain.enums.CategoryType;
import organize.organizeJPA_study_1.domain.enums.ItemStatus;
import organize.organizeJPA_study_1.domain.itemtype.Album;
import organize.organizeJPA_study_1.domain.itemtype.Book;
import organize.organizeJPA_study_1.dto.AlbumCreateDto;
import organize.organizeJPA_study_1.dto.BookCreateDto;
import organize.organizeJPA_study_1.dto.ItemCreateDto;
import organize.organizeJPA_study_1.mapper.Mapper;
import organize.organizeJPA_study_1.repository.CategoryItemRepository;
import organize.organizeJPA_study_1.repository.CategoryRepository;
import organize.organizeJPA_study_1.repository.ItemRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryItemRepository categoryItemRepository;
    @Autowired
    Mapper<ItemCreateDto, Item> itemMapper;

    @Test
    @DisplayName("아이템 저장 성공")
    void saveTest_success() {
        // given
        Category category1 = Category.builder().categoryType(CategoryType.BOOK).build();
        Category category2 = Category.builder().categoryType(CategoryType.ALBUM).build();
        Category category3 = Category.builder().categoryType(CategoryType.MOVIE).build();
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        BookCreateDto bookDto = new BookCreateDto(
                "JPA 책",
                15000,
                5,
                Arrays.asList(CategoryType.BOOK, CategoryType.MOVIE),
                "김영한",
                "123-123"
        );

        // when
        Long itemId = itemService.saveItem(bookDto);

        // then
        Book item = (Book) itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("searchException"));
        boolean hasBook = item.getCategoryItems().stream()
                .anyMatch(categoryItem -> categoryItem.getCategory().getCategoryType() == CategoryType.BOOK);
        boolean hasMovie = item.getCategoryItems().stream()
                .anyMatch(categoryItem -> categoryItem.getCategory().getCategoryType() == CategoryType.MOVIE);

        assertThat(item.getName()).isEqualTo(bookDto.getName());
        assertThat(item.getPrice()).isEqualTo(bookDto.getPrice());
        assertThat(item.getStockQuantity()).isEqualTo(bookDto.getStockQuantity());
        assertThat(item.getAuthor()).isEqualTo(bookDto.getAuthor());
        assertThat(item.getIsbn()).isEqualTo(bookDto.getIsbn());
        assertThat(item.getItemStatus()).isEqualTo(ItemStatus.ON_SALE);
        assertThat(item.getCategoryItems().size()).isEqualTo(2);
        assertThat(hasBook).isTrue();
        assertThat(hasMovie).isTrue();
    }

    @Test
    void offSaleTest_success() {
        // given
        Category category1 = Category.builder().categoryType(CategoryType.BOOK).build();
        Category category2 = Category.builder().categoryType(CategoryType.ALBUM).build();
        Category category3 = Category.builder().categoryType(CategoryType.MOVIE).build();
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        BookCreateDto bookDto = new BookCreateDto(
                "JPA 책",
                15000,
                5,
                Arrays.asList(CategoryType.BOOK, CategoryType.MOVIE),
                "김영한",
                "123-123"
        );

        Long itemId = itemService.saveItem(bookDto);

        // when
        itemService.offSaleItem(itemId);

        // then
        Book item = (Book) itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("searchException"));
        boolean hasBook = item.getCategoryItems().stream()
                .anyMatch(categoryItem -> categoryItem.getCategory().getCategoryType() == CategoryType.BOOK);
        boolean hasMovie = item.getCategoryItems().stream()
                .anyMatch(categoryItem -> categoryItem.getCategory().getCategoryType() == CategoryType.MOVIE);

        assertThat(item.getName()).isEqualTo(bookDto.getName());
        assertThat(item.getPrice()).isEqualTo(bookDto.getPrice());
        assertThat(item.getStockQuantity()).isEqualTo(bookDto.getStockQuantity());
        assertThat(item.getAuthor()).isEqualTo(bookDto.getAuthor());
        assertThat(item.getIsbn()).isEqualTo(bookDto.getIsbn());
        assertThat(item.getItemStatus()).isEqualTo(ItemStatus.OFF_SALE);
        assertThat(item.getCategoryItems().size()).isEqualTo(2);
        assertThat(hasBook).isTrue();
        assertThat(hasMovie).isTrue();

    }

    @Test
    @DisplayName("아이템 수정 성공")
    void updateTest_success() {
        // given
        Category category1 = Category.builder().categoryType(CategoryType.BOOK).build();
        Category category2 = Category.builder().categoryType(CategoryType.ALBUM).build();
        Category category3 = Category.builder().categoryType(CategoryType.MOVIE).build();
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        BookCreateDto bookDto = new BookCreateDto(
                "JPA 책",
                15000,
                5,
                Arrays.asList(CategoryType.BOOK, CategoryType.MOVIE),
                "김영한",
                "123-123"
        );
        Long itemId = itemService.saveItem(bookDto);
        AlbumCreateDto albumDto = new AlbumCreateDto(
                "분홍신",
                15000,
                5,
                Arrays.asList(CategoryType.ALBUM, CategoryType.MOVIE),
                "아이유",
                "아이유 노래"
        );
        // when
        itemService.updateItem(itemId, albumDto);

        // then
        List<Item> items = itemRepository.findAll();
        assertThat(items.size()).isEqualTo(1);
        Album album = (Album) items.get(0);
        boolean hasAlbum = album.getCategoryItems().stream()
                .anyMatch(categoryItem -> categoryItem.getCategory().getCategoryType() == CategoryType.ALBUM);
        boolean hasMovie = album.getCategoryItems().stream()
                .anyMatch(categoryItem -> categoryItem.getCategory().getCategoryType() == CategoryType.MOVIE);

        assertThat(album.getName()).isEqualTo(albumDto.getName());
        assertThat(album.getPrice()).isEqualTo(albumDto.getPrice());
        assertThat(album.getStockQuantity()).isEqualTo(albumDto.getStockQuantity());
        assertThat(album.getArtist()).isEqualTo(albumDto.getArtist());
        assertThat(album.getEtc()).isEqualTo(albumDto.getEtc());
        assertThat(album.getItemStatus()).isEqualTo(ItemStatus.ON_SALE);
        assertThat(album.getCategoryItems().size()).isEqualTo(2);
        assertThat(hasAlbum).isTrue();
        assertThat(hasMovie).isTrue();
    }
}