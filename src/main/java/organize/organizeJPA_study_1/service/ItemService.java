package organize.organizeJPA_study_1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import organize.organizeJPA_study_1.domain.Category;
import organize.organizeJPA_study_1.domain.CategoryItem;
import organize.organizeJPA_study_1.domain.Item;
import organize.organizeJPA_study_1.domain.enums.CategoryType;
import organize.organizeJPA_study_1.domain.enums.UseYn;
import organize.organizeJPA_study_1.dto.ItemCreateDto;
import organize.organizeJPA_study_1.dto.ItemResponse;
import organize.organizeJPA_study_1.mapper.Mapper;
import organize.organizeJPA_study_1.repository.CategoryRepository;
import organize.organizeJPA_study_1.repository.ItemRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
/***
 * 1. 저장 (C)
 * 2. 판매 상태 변경 (U)
 * 3. 아이템 수정 (U)
 * 4. 아이템 삭제 (U - D)
 * 5. 아이템 전체 조회 (R)
 * 6. 아이템 하나 조회 (R)
 */ public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final Mapper<ItemCreateDto, Item> itemMapper;

    /***
     * 판매 물품 저장
     * @param itemCreateDto 저장할 아이템 데이터
     * @return 저장된 아이템 ID
     */
    @Transactional
    public Long saveItem(ItemCreateDto itemCreateDto) {
        List<CategoryType> types = itemCreateDto.getCategoryTypes();
        List<Category> categories = categoryRepository.findByCategoryTypeIn(types);
        if (categories.isEmpty()) {
            throw new IllegalArgumentException("카테고리는 필수 입력 사항입니다.");
        }
        List<CategoryItem> categoryItems = categories.stream().map(CategoryItem::of).toList();
        Item item = itemMapper.toEntity(itemCreateDto);
        item.addCategoryItems(categoryItems);
        Item saveItem = itemRepository.save(item);
        return saveItem.getId();
    }

    /***
     * 판매 상태 변경
     * @param itemId 변경할 아이템 ID
     */
    @Transactional
    public void toggleSaleStatus(Long itemId) {
        findByItemId(itemId).changeSale();
    }

    /***
     * 아이템 삭제
     * @param itemId 삭제할 아이템 ID
     */
    @Transactional
    public void deleteItem(Long itemId) {
        findByItemId(itemId).notUse();
    }

    /***
     * 아이템 수정
     * Single Table Update : dirty checking
     * @param itemId 수정할 아이템 ID
     * @param itemCreateDto 수정할 아이템 데이터
     */
    @Transactional
    public void updateItem(Long itemId, ItemCreateDto itemCreateDto) {
        Item item = findByItemId(itemId);
        if (!item.isValidUpdate(itemCreateDto.getClass())) {
            item.notUse();
            saveItem(itemCreateDto);
        } else {
            item.updateItem(itemCreateDto);
        }
    }

    /***
     * 사용 중인 모든 아이템 조회
     * @return 사용 중인 아이템 목록
     */
    public List<ItemResponse> findAllUseY() {
        return Item.entityToResponseDto(itemRepository.findAllUseY(UseYn.Y));
    }

    /***
     * 특정 아이템 조회
     * @param itemId 조회할 아이템 ID
     * @return 조회된 아이템
     */
    public Item findItemById(Long itemId) {
        return itemRepository.findById(itemId).orElse(null);
    }

    private Item findByItemId(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));
    }

}
