package org.example.aiplaner.Service;

import org.example.aiplaner.Entity.Todoitem;
import org.example.aiplaner.dao.Itemdao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ItemService {
    @Autowired
    private Itemdao itemdao;

    public ItemService (Itemdao itemdao) {
        this.itemdao = itemdao;
    }

    public List<Todoitem> GetAllTodoItem(Integer userId) {
        return itemdao.findByUserID(userId);
    }

    public Optional<Todoitem> SaveTodoItem(Todoitem todoitem) {
        Optional<Todoitem> item=Optional.of(itemdao.save(todoitem));
        if(item.isPresent()){
            return item;
        }
        return null;
    }

    public boolean DeleteTodoItem(int userId) {
        List<Todoitem> todoitems =itemdao.findByUserID(userId);
        Todoitem todoitem=todoitems.get(0);
        itemdao.delete(todoitem);
        return true;
    }

    public Optional<Todoitem> UpdateItem(Todoitem todoitem) {
        return Optional.of(itemdao.save(todoitem));
    }
}
