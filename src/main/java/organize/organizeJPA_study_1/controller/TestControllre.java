package organize.organizeJPA_study_1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import organize.organizeJPA_study_1.domain.Category;
import organize.organizeJPA_study_1.dto.ItemCreateDto;
import organize.organizeJPA_study_1.repository.CategoryRepository;
import organize.organizeJPA_study_1.service.ItemService;

@RestController
@RequiredArgsConstructor
public class TestControllre {
    private final CategoryRepository categoryRepository;
    private final ItemService itemService;

    @PostMapping
    public void a(@RequestBody ItemCreateDto dto) {
        itemService.saveItem(dto);
    }

    @PostMapping("/c")
    public void b(@RequestBody Category dto) {
        categoryRepository.save(dto);
    }

    @PostMapping("/u/{itemId}")
    public void u(@PathVariable("itemId") Long itemId, @RequestBody ItemCreateDto dto) {
        itemService.updateItem(itemId, dto);
    }

    @GetMapping("/off/{itemId}")
    public void off(@PathVariable("itemId") Long itemId) {
        itemService.offSaleItem(itemId);
    }
}
