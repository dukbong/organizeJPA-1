package organize.organizeJPA_study_1.mapper;

import org.springframework.stereotype.Component;
import organize.organizeJPA_study_1.domain.Category;
import organize.organizeJPA_study_1.domain.Item;
import organize.organizeJPA_study_1.domain.subtype.Album;
import organize.organizeJPA_study_1.domain.subtype.Book;
import organize.organizeJPA_study_1.domain.subtype.Movie;
import organize.organizeJPA_study_1.dto.request.subtype.AlbumRequest;
import organize.organizeJPA_study_1.dto.request.subtype.BookRequest;
import organize.organizeJPA_study_1.dto.request.ItemRequest;
import organize.organizeJPA_study_1.dto.request.subtype.MovieRequest;
import organize.organizeJPA_study_1.dto.response.subtype.AlbumResponse;
import organize.organizeJPA_study_1.dto.response.subtype.BookResponse;
import organize.organizeJPA_study_1.dto.response.ItemResponse;
import organize.organizeJPA_study_1.dto.response.subtype.MovieResponse;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper implements Mapper<ItemRequest, Item, ItemResponse> {


    @Override
    public Item toEntity(ItemRequest dto) {
        if(dto instanceof BookRequest bookCreateDto) {
            return Book.builder()
                    .name(bookCreateDto.getName())
                    .price(bookCreateDto.getPrice())
                    .stockQuantity(bookCreateDto.getStockQuantity())
                    .author(bookCreateDto.getAuthor())
                    .isbn(bookCreateDto.getIsbn())
                    .build();
        }
        if(dto instanceof AlbumRequest albumCreateDto) {
            return Album.builder()
                    .name(albumCreateDto.getName())
                    .price(albumCreateDto.getPrice())
                    .stockQuantity(albumCreateDto.getStockQuantity())
                    .artist(albumCreateDto.getArtist())
                    .etc(albumCreateDto.getEtc())
                    .build();
        }
        if(dto instanceof MovieRequest movieCreateDto) {
            return Movie.builder()
                    .name(movieCreateDto.getName())
                    .price(movieCreateDto.getPrice())
                    .stockQuantity(movieCreateDto.getStockQuantity())
                    .director(movieCreateDto.getDirector())
                    .actor(movieCreateDto.getActor())
                    .build();
        }
        throw new IllegalArgumentException("지원하지 않는 아이템 형태입니다.");
    }

    @Override
    public ItemRequest toDto(Item entity) {
        return null;
    }

    @Override
    public List<ItemResponse> toResponseListDto(List<Item> entities) {
        List<ItemResponse> responses = new ArrayList<>();
        for (Item item : entities) {
            ItemResponse response = null;
            if (item instanceof Book book) {
                String mainCategory = mainCategoryType(book);
                List<String> categoryList = categoryList(book, mainCategory);
                response = BookResponse.builder().name(book.getName())
                        .price(book.getPrice())
                        .stockQuantity(book.getStockQuantity())
                        .mainCategory(mainCategory)
                        .categoryList(categoryList)
                        .itemStatus(book.getItemStatus())
                        .author(book.getAuthor())
                        .isbn(book.getIsbn())
                        .build();
            }
            if (item instanceof Movie movie) {
                String mainCategory = mainCategoryType(movie);
                List<String> categoryList = categoryList(movie, mainCategory);
                response = MovieResponse.builder().name(movie.getName())
                        .price(movie.getPrice())
                        .stockQuantity(movie.getStockQuantity())
                        .mainCategory(mainCategory)
                        .categoryList(categoryList)
                        .itemStatus(movie.getItemStatus())
                        .director(movie.getDirector())
                        .actor(movie.getActor())
                        .build();
            }
            if (item instanceof Album album) {
                String mainCategory = mainCategoryType(album);
                List<String> categoryList = categoryList(album, mainCategory);
                response = AlbumResponse.builder().name(album.getName())
                        .price(album.getPrice())
                        .stockQuantity(album.getStockQuantity())
                        .mainCategory(mainCategory)
                        .categoryList(categoryList)
                        .itemStatus(album.getItemStatus())
                        .artist(album.getArtist())
                        .etc(album.getEtc())
                        .build();
            }

            if (response != null) {
                responses.add(response);
            }
        }
        return responses;
    }

    private static String mainCategoryType(Item item) {
        return item.getCategoryItems().stream().map(row -> {
            Category parentCategory = row.getCategory().getParent();
            if (parentCategory != null) {
                return parentCategory.getCategoryType().getType();
            } else {
                return row.getCategory().getCategoryType().getType();
            }
        }).findFirst().orElseThrow(() -> new IllegalArgumentException("메인 카테고리가 존재하지 않습니다."));
    }

    private static List<String> categoryList(Item item, String mainCategory) {
        return item.getCategoryItems().stream().map(row -> row.getCategory().getCategoryType().getType())
                .filter(type -> !type.equals(mainCategory))
                .toList();
    }

}