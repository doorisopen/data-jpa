package study.datajpa.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.ItemRepository;

@Transactional
@SpringBootTest
class ItemTest {

    @Autowired
    ItemRepository itemRepository;

    void save() {
        Item item = new Item();
        itemRepository.save(item);
    }

}