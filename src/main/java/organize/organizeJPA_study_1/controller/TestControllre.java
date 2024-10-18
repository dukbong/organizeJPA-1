package organize.organizeJPA_study_1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
