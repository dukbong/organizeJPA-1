package organize.organizeJPA_study_1.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import organize.organizeJPA_study_1.domain.Item;
import organize.organizeJPA_study_1.repository.ItemRepository;

@SpringBootTest
@Transactional
public class ItemSelectTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    @Rollback(false)
    void selectTest1() {
        Long startTime = System.currentTimeMillis();
        a();
        Long endTime = System.currentTimeMillis();
        System.out.println("readOnly = true 총 시간 : " + (endTime - startTime));
    }
    @Test
    @Rollback(false)
    void selectTest2() {
        Long startTime = System.currentTimeMillis();
        b();
        Long endTime = System.currentTimeMillis();
        System.out.println("readOnly = false 총 시간 : " + (endTime - startTime));
    }
    @Test
    @Rollback(false)
    void selectTest3() {
        Long startTime = System.currentTimeMillis();
        c();
        Long endTime = System.currentTimeMillis();
        System.out.println("readOnly = false 총 시간 : " + (endTime - startTime));
    }
    @Test
    @Rollback(false)
    void selectTest4() {
        Long startTime = System.currentTimeMillis();
        d();
        Long endTime = System.currentTimeMillis();
        System.out.println("readOnly = false 총 시간 : " + (endTime - startTime));
    }

    @Transactional
    protected void a() {
        Item item = readOnlyFalse(1966L);
        item.notUse();
    }

    @Transactional
    protected void b() {
        Item item = readOnlyTrue(3846L);
        item.notUse();
    }

    @Transactional
    protected void c() {
        Item item = readOnlyNewTrue(14856L);
        item.notUse();
    }

    @Transactional
    protected void d() {
        Item item = privateMethod(5264L);
        item.notUse();
    }


    protected Item readOnlyFalse(Long itemId){
        return itemRepository.findById(itemId).orElseThrow( () -> new IllegalArgumentException("예외"));
    }

    @Transactional(readOnly = true)
    protected Item readOnlyTrue(Long itemId){
        return itemRepository.findById(itemId).orElseThrow( () -> new IllegalArgumentException("예외"));
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    protected Item readOnlyNewTrue(Long itemId){
        return itemRepository.findById(itemId).orElseThrow( () -> new IllegalArgumentException("예외"));
    }
    private Item privateMethod(Long itemId){
        return itemRepository.findById(itemId).orElseThrow( () -> new IllegalArgumentException("예외"));
    }
}
