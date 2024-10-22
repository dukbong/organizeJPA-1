package organize.organizeJPA_study_1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import organize.organizeJPA_study_1.domain.Category;
import organize.organizeJPA_study_1.dto.CategoryDto;
import organize.organizeJPA_study_1.dto.ItemCreateDto;
import organize.organizeJPA_study_1.dto.ItemResponse;
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
    public void a(@Valid @RequestBody ItemCreateDto dto) {
        itemService.saveItem(dto);
    }

    @PostMapping("/cp")
    public void cp(@RequestBody CategoryDto dto) {
        categoryService.saveHighCategory(dto);
    }

    @PostMapping("/u/{itemId}")
    public void u(@PathVariable("itemId") Long itemId, @RequestBody ItemCreateDto dto) {
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
}
