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
import organize.organizeJPA_study_1.domain.subtype.Album;
import organize.organizeJPA_study_1.domain.subtype.Book;
import organize.organizeJPA_study_1.dto.request.subtype.AlbumRequest;
import organize.organizeJPA_study_1.dto.request.subtype.BookRequest;
import organize.organizeJPA_study_1.dto.request.ItemRequest;
import organize.organizeJPA_study_1.dto.response.ItemResponse;
import organize.organizeJPA_study_1.mapper.Mapper;
import organize.organizeJPA_study_1.repository.CategoryItemRepository;
import organize.organizeJPA_study_1.repository.CategoryRepository;
import organize.organizeJPA_study_1.repository.ItemRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
    Mapper<ItemRequest, Item, ItemResponse> itemMapper;

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

        BookRequest bookDto = new BookRequest(
                "JPA 책",
                15000,
                5,
                CategoryType.BOOK,
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

        BookRequest bookDto = new BookRequest(
                "JPA 책",
                15000,
                5,
                CategoryType.BOOK,
                Arrays.asList(CategoryType.BOOK, CategoryType.MOVIE),
                "김영한",
                "123-123"
        );

        Long itemId = itemService.saveItem(bookDto);

        // when
        itemService.toggleSaleStatus(itemId);

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

        BookRequest bookDto = new BookRequest(
                "JPA 책",
                15000,
                5,
                CategoryType.BOOK,
                Arrays.asList(CategoryType.BOOK, CategoryType.MOVIE),
                "김영한",
                "123-123"
        );
        Long itemId = itemService.saveItem(bookDto);
        AlbumRequest albumDto = new AlbumRequest(
                "분홍신",
                15000,
                5,
                CategoryType.ALBUM,
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