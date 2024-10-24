package organize.organizeJPA_study_1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import organize.organizeJPA_study_1.dto.request.CategoryRequest;
import organize.organizeJPA_study_1.dto.request.ItemRequest;
import organize.organizeJPA_study_1.dto.response.ItemResponse;
import organize.organizeJPA_study_1.repository.CategoryRepository;
import organize.organizeJPA_study_1.service.CategoryService;
import organize.organizeJPA_study_1.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestControllre {
    private final CategoryRepository categoryRepository;
    private final ItemService itemService;

    private final CategoryService categoryService;

    @PostMapping
    public void a(@Valid @RequestBody ItemRequest dto) {
        itemService.saveItem(dto);
    }

    @PostMapping("/cp")
    public void cp(@RequestBody CategoryRequest dto) {
        categoryService.saveHighCategory(dto);
    }

    @PostMapping("/u/{itemId}")
    public void u(@PathVariable("itemId") Long itemId, @RequestBody ItemRequest dto) {
        itemService.updateItem(itemId, dto);
    }

    @GetMapping("/off/{itemId}")
    public void off(@PathVariable("itemId") Long itemId) {
        itemService.toggleSaleStatus(itemId);
    }

    @GetMapping
    public List<ItemResponse> g() {
        return itemService.findAllUseY();
    }

    @GetMapping("/cdh/{highCategoryId}")
    public void deleteHighCategory(@PathVariable("highCategoryId") Long highCategoryId) {
        categoryService.deleteHighCategory(highCategoryId);
    }

    @GetMapping("/cds/{rowCategoryId}")
    public void deleteRowCategory(@PathVariable("rowCategoryId") Long rowCategoryId) {
        categoryService.deleteRowCategory(rowCategoryId);
    }

}
