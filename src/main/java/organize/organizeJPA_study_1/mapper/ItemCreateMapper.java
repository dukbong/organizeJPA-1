package organize.organizeJPA_study_1.mapper;

import org.springframework.stereotype.Component;
import organize.organizeJPA_study_1.domain.Item;
import organize.organizeJPA_study_1.domain.Member;
import organize.organizeJPA_study_1.domain.embed.Address;
import organize.organizeJPA_study_1.domain.itemtype.Album;
import organize.organizeJPA_study_1.domain.itemtype.Book;
import organize.organizeJPA_study_1.domain.itemtype.Movie;
import organize.organizeJPA_study_1.dto.*;

@Component
public class ItemCreateMapper implements Mapper<ItemCreateDto, Item> {


    @Override
    public Item toEntity(ItemCreateDto dto) {
        if(dto instanceof BookCreateDto bookCreateDto) {
            return Book.builder()
                    .name(bookCreateDto.getName())
                    .price(bookCreateDto.getPrice())
                    .stockQuantity(bookCreateDto.getStockQuantity())
                    .author(bookCreateDto.getAuthor())
                    .isbn(bookCreateDto.getIsbn())
                    .build();
        }
        if(dto instanceof AlbumCreateDto albumCreateDto) {
            return Album.builder()
                    .name(albumCreateDto.getName())
                    .price(albumCreateDto.getPrice())
                    .stockQuantity(albumCreateDto.getStockQuantity())
                    .artist(albumCreateDto.getArtist())
                    .etc(albumCreateDto.getEtc())
                    .build();
        }
        if(dto instanceof MovieCreateDto movieCreateDto) {
            return Movie.builder()
                    .name(movieCreateDto.getName())
                    .price(movieCreateDto.getPrice())
                    .stockQuantity(movieCreateDto.getStockQuantity())
                    .director(movieCreateDto.getDirector())
                    .actor(movieCreateDto.getActor())
                    .build();
        }

        throw new IllegalArgumentException("ItemCreateDto Type :" + dto.getClass().getSimpleName());
    }

    @Override
    public ItemCreateDto toDto(Item entity) {
        return null;
    }

}