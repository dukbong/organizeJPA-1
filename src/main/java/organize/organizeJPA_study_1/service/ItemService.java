package organize.organizeJPA_study_1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import organize.organizeJPA_study_1.domain.Category;
import organize.organizeJPA_study_1.domain.CategoryItem;
import organize.organizeJPA_study_1.domain.Item;
import organize.organizeJPA_study_1.domain.enums.CategoryType;
import organize.organizeJPA_study_1.dto.ItemCreateDto;
import organize.organizeJPA_study_1.mapper.Mapper;
import organize.organizeJPA_study_1.repository.CategoryItemRepository;
import organize.organizeJPA_study_1.repository.CategoryRepository;
import organize.organizeJPA_study_1.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryItemRepository CategoryItemRepository;
    private final Mapper<ItemCreateDto, Item> itemMapper;

    public Long saveItem(ItemCreateDto itemCreateDto) {
        List<CategoryType> types = itemCreateDto.getCategoryTypes();
        List<Category> categories = categoryRepository.findByCategoryTypeIn(types);
        List<CategoryItem> categoryItems = categories.stream().map(CategoryItem::of).toList();
        Item item = itemMapper.toEntity(itemCreateDto);
        categoryItems.forEach(item::addCategoryItem);
        Item saveItem = itemRepository.save(item);
        CategoryItemRepository.saveAll(categoryItems);
        return saveItem.getId();
    }

    public void offSaleItem(Long itemId) {
        itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")).offSale();
    }

}
