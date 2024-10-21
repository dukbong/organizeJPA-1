package organize.organizeJPA_study_1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import organize.organizeJPA_study_1.domain.Category;
import organize.organizeJPA_study_1.domain.CategoryItem;
import organize.organizeJPA_study_1.domain.Item;
import organize.organizeJPA_study_1.domain.enums.CategoryType;
import organize.organizeJPA_study_1.domain.enums.ItemStatus;
import organize.organizeJPA_study_1.domain.itemtype.Album;
import organize.organizeJPA_study_1.domain.itemtype.Book;
import organize.organizeJPA_study_1.domain.itemtype.Movie;
import organize.organizeJPA_study_1.dto.AlbumCreateDto;
import organize.organizeJPA_study_1.dto.BookCreateDto;
import organize.organizeJPA_study_1.dto.ItemCreateDto;
import organize.organizeJPA_study_1.dto.MovieCreateDto;
import organize.organizeJPA_study_1.mapper.Mapper;
import organize.organizeJPA_study_1.repository.CategoryRepository;
import organize.organizeJPA_study_1.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final Mapper<ItemCreateDto, Item> itemMapper;

    /***
     * 판매 물품 저장
     * @param itemCreateDto
     * @return
     */
    @Transactional
    public Long saveItem(ItemCreateDto itemCreateDto) {
        List<CategoryType> types = itemCreateDto.getCategoryTypes();
        List<Category> categories = categoryRepository.findByCategoryTypeIn(types);
        List<CategoryItem> categoryItems = categories.stream().map(CategoryItem::of).toList();
        Item item = itemMapper.toEntity(itemCreateDto);
        item.addCategoryItems(categoryItems);
        Item saveItem = itemRepository.save(item);
        return saveItem.getId();
    }

    /***
     * 판매 중지
     * @param itemId
     */
    @Transactional
    public void offSaleItem(Long itemId) {
        itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")).offSale();
    }

    /***
     * Single Table Update : dirty checking
     * @param itemId
     * @param itemCreateDto
     */
    @Transactional
    public void updateItem(Long itemId, ItemCreateDto itemCreateDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("아이템을 찾을 수 없습니다."));

        Class<? extends ItemCreateDto> dtoClass = itemCreateDto.getClass();
        if (!isValidUpdate(item, dtoClass)) {
            itemRepository.delete(item);
            saveItem(itemCreateDto);
        } else {
            item.updateItem(itemCreateDto);
        }
    }

    private boolean isValidUpdate(Item item, Class<? extends ItemCreateDto> dtoClass) {
        if (item instanceof Book && dtoClass != BookCreateDto.class) {
            return false;
        }
        if (item instanceof Movie && dtoClass != MovieCreateDto.class) {
            return false;
        }
        if (item instanceof Album && dtoClass != AlbumCreateDto.class) {
            return false;
        }
        return true;
    }

    public List<Item> findItem() {
        return itemRepository.findAllOn(ItemStatus.ON_SALE);
    }

    public Item findItemById(Long itemId) {
        return itemRepository.findById(itemId).orElse(null);
    }

}
