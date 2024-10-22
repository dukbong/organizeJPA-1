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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
/***
 * 1. 저장 (C)
 * 2. 판매 상태 변경 (U) : 판매중, 판매중지
 * 3. 아이템 수정 (U)
 * 4. 아이템 삭제 (U - D) : Y, N
 * 5. 아이템 전체 조회 (R) : where useYn = 'Y'
 * 6. 아이템 하나 조회 (R)
 */ public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final Mapper<ItemCreateDto, Item, ItemResponse> itemMapper;

    /***
     * 판매 물품 저장
     * @param itemCreateDto 저장할 아이템 데이터
     * @return 저장된 아이템 ID
     */
    @Transactional
    public Long saveItem(ItemCreateDto itemCreateDto) {
        CategoryType mainType = itemCreateDto.getMainCategoryType();
        List<CategoryType> types = itemCreateDto.getCategoryTypes();
        List<Category> mainCategory = categoryRepository.findByCategoryTypeIn(List.of(mainType));
        if(mainCategory.isEmpty()) {
            throw new IllegalArgumentException("상위 카테고리를 찾을 수 없습니다.");
        }
        List<Category> categories = categoryRepository.findByMainTypeAndCategoryTypeIn(types, mainCategory.get(0).getId());
        if(!(categories.size() == types.size())) {
            throw new IllegalArgumentException("하위 카테고리 중 일부를 찾을 수 없습니다.");
        }
        Item item = itemMapper.toEntity(itemCreateDto);
        if(!categories.isEmpty()) {
            List<CategoryItem> categoryItems = categories.stream().map(CategoryItem::of).toList();
            item.addCategoryItems(categoryItems);
        } else {
            List<CategoryItem> mainCategoryItems = mainCategory.stream().map(CategoryItem::of).toList();
            item.addCategoryItems(mainCategoryItems);
        }
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
            boolean sizeCheck = item.getCategoryItems().size() == itemCreateDto.getCategoryTypes().size();
            boolean equalsCheck = sizeCheck && equalsCheck(item, itemCreateDto);
            if(!sizeCheck || !equalsCheck) {
                item.getCategoryItems().clear();
                List<CategoryType> types = itemCreateDto.getCategoryTypes();
                CategoryType mainType = itemCreateDto.getMainCategoryType();
                List<Category> mainCategory = categoryRepository.findByCategoryTypeIn(List.of(mainType));
                if(mainCategory.isEmpty()) {
                    throw new IllegalArgumentException("상위 카테고리를 찾을 수 없습니다.");
                }
                List<CategoryItem> lc = new ArrayList<>();
                for (CategoryType categoryType : types) {
                    List<Category> categories = categoryRepository.findByMainTypeAndCategoryTypeIn(List.of(categoryType),mainCategory.get(0).getId());
                    lc.add(CategoryItem.of(categories.get(0)));
                }
                item.addCategoryItems(lc);
            }
            item.updateItem(itemCreateDto);
        }
    }

    private boolean equalsCheck(Item item, ItemCreateDto itemCreateDto) {
        List<CategoryItem> categoryItems = item.getCategoryItems();
        List<CategoryType> categoryTypesFromItem = categoryItems.stream()
                .map(categoryItem -> categoryItem.getCategory().getCategoryType()) // CategoryType 추출
                .toList();

        List<CategoryType> categoryTypesFromDto = itemCreateDto.getCategoryTypes();

        return new HashSet<>(categoryTypesFromItem).containsAll(categoryTypesFromDto);
    }

    /***
     * 사용 중인 모든 아이템 조회
     * @return 사용 중인 아이템 목록
     */
    public List<ItemResponse> findAllUseY() {
        return itemMapper.toResponseListDto(itemRepository.findAllUseY(UseYn.Y));
//        return Item.entityToResponseDto(itemRepository.findAllUseY(UseYn.Y));
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
        Optional<Item> findItem =  itemRepository.findByIdAndUseY(itemId, UseYn.Y);
        if(findItem.isEmpty()) {
            throw new IllegalArgumentException("해당 상품이 존재하지 않습니다.");
        }
        return findItem.get();
    }

}
