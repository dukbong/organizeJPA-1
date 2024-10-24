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
import organize.organizeJPA_study_1.dto.request.ItemRequest;
import organize.organizeJPA_study_1.dto.response.ItemResponse;
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
 * 2. 판매 상태 변경 (U) : 판매중, 판매중지
 * 3. 아이템 수정 (U)
 * 4. 아이템 삭제 (U - D) : Y, N
 * 5. 아이템 전체 조회 (R) : where useYn = 'Y'
 * 6. 아이템 하나 조회 (R)
 */ public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final Mapper<ItemRequest, Item, ItemResponse> itemMapper;

    /***
     * 판매 물품 저장
     * @param itemCreateDto 저장할 아이템 데이터
     * @return 저장된 아이템 ID
     */
    @Transactional
    public Long saveItem(ItemRequest itemCreateDto) {
        List<CategoryType> subCategoryType = itemCreateDto.getCategoryTypes();
        Category mainCategory = findByMainCategoryTypeToEquals(itemCreateDto.getMainCategoryType());
        List<Category> subCategories = findByMainTypeAndCategoryTypeIn(subCategoryType, mainCategory.getId());
        Item item = itemMapper.toEntity(itemCreateDto);
        if(!subCategories.isEmpty()) {
            // 하위 카테고리와 상위 카테고리가 존재한다.
            if(!(subCategories.size() == subCategoryType.size())) throw new IllegalArgumentException("하위 카테고리 중 일부를 찾을 수 없습니다.");
            item.addTotalCategoryItems(CategoryItem.of(subCategories));
        } else {
            // 상위 카테고리만 존재한다.
            CategoryItem mainCategoryItems = CategoryItem.of(mainCategory);
            item.addMainCategoryItems(mainCategoryItems);
        }
        return itemRepository.save(item).getId();
    }

    /***
     * 판매 상태 변경
     * @param itemId 변경할 아이템 ID
     */
    @Transactional
    public void toggleSaleStatus(Long itemId) {
        findByIdUseY(itemId).toggleSaleStatus();
    }

    /***
     * 아이템 삭제
     * @param itemId 삭제할 아이템 ID
     */
    @Transactional
    public void deleteItem(Long itemId) {
        findByIdUseY(itemId).notUse();
    }

    /***
     * 아이템 수정
     * Single Table Update : dirty checking
     * @param itemId 수정할 아이템 ID
     * @param itemCreateDto 수정할 아이템 데이터
     */
    @Transactional
    public void updateItem(Long itemId, ItemRequest itemCreateDto) {
        Item item = findByIdUseY(itemId);
        if (!item.isValidUpdate(itemCreateDto.getClass())) {
            saveItem(itemCreateDto);
        } else {
            if(!item.areCategoryItemsAndTypesEqual(itemCreateDto)) {
                List<CategoryType> types = itemCreateDto.getCategoryTypes();
                CategoryType mainType = itemCreateDto.getMainCategoryType();
                Category mainCategory = findByMainCategoryTypeToEquals(mainType);
                List<Category> totalCategory = findByMainTypeAndCategoryTypeIn(types,mainCategory.getId());
                item.addTotalCategoryItems(CategoryItem.of(totalCategory));
            }
            item.updateItem(itemCreateDto);
        }
    }

    /***
     * 사용 중이며, 판매 상태는 상관없이 전체 조회
     * @return 사용 중인 아이템 목록
     */
    public List<ItemResponse> findAllUseY() {
        return itemMapper.toResponseListDto(itemRepository.findAllUseY(UseYn.Y));
    }

    /***
     * 사용 여부와 상관없이 특정 아이템 조회
     * @param itemId 조회할 아이템 ID
     * @return 조회된 아이템
     */
    public Item findItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
    }

    /***
     * 사용 여부 = Y이며, 판매중인 전체 아이템 조회
     * @return 조회된 아이템
     */
    public List<ItemResponse> finaAllUseYAndOnSale() {
        return itemMapper.toResponseListDto(itemRepository.finaAllUseYAndOnSale());
    }

    /***
     * 타입으로 특정 카테고리 조회
     * @param type 조회할 카테고리 이름
     * @return 조회된 카테고리
     */
    protected Category findByMainCategoryTypeToEquals(CategoryType type) {
        return categoryRepository.findByMainCategoryTypeToEquals(type).orElseThrow(
                () -> new IllegalArgumentException("상위 카테고리를 찾을 수 없습니다.")
        );
    }

    /***
     * 타입 리스트와 일치하면서 메인 카테고리로 조회
     * @param types 조회할 타입 리스트
     * @param categoryId 조회할 카테고리 아이디
     * @return 조회된 카테고리 리스트
     */
    protected List<Category> findByMainTypeAndCategoryTypeIn(List<CategoryType> types, Long categoryId) {
        return categoryRepository.findByMainTypeAndCategoryTypeIn(types, categoryId);
    }

    /***
     * 사용 가능한 특정 아이템 조회
     * @param itemId 조회할 아이템 Id
     * @return 조회된 아이템
     */
    protected Item findByIdUseY(Long itemId) {
        return itemRepository.findByIdAndUseY(itemId, UseYn.Y).orElseThrow(
                () -> new IllegalArgumentException("해당 상품이 존재하지 않습니다.")
        );
    }
}
