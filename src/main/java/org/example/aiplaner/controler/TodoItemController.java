package org.example.aiplaner.controler;


import org.apache.coyote.Request;
import org.apache.coyote.Response;
import org.example.aiplaner.DTO.ItemMessage;
import org.example.aiplaner.DTO.LogMessage;
import org.example.aiplaner.Entity.Todoitem;
import org.example.aiplaner.Entity.UserEntity;
import org.example.aiplaner.Service.ItemService;
import org.example.aiplaner.dao.Itemdao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/Todo")
class TodoItemController {
    @Autowired
    private ItemService itemService;


    public TodoItemController(ItemService itemService) {
        this.itemService = itemService;

    }

    @GetMapping("/getAll")
    public ResponseEntity<Map<String, Object>> ReGetAllItem(@RequestAttribute Long userId) {
        System.out.println(userId);
        List<Todoitem> todoitems=itemService.GetAllTodoItem(Math.toIntExact(userId));
        Map<String,Object> response=new HashMap<>();
        response.put("status",HttpStatus.OK);
        response.put("data",todoitems);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> SaveItem(@RequestAttribute Long userId , @RequestBody ItemMessage itemMessage) {
        int userid= Math.toIntExact(userId);
        Todoitem todoitem=new Todoitem();
        System.out.println(itemMessage.getcontent());
        todoitem.setContont(itemMessage.getcontent());
        todoitem.setFinish(false);
        todoitem.setDeadline(itemMessage.getDeadline());
        todoitem.setImportance(itemMessage.getImportance());
        todoitem.setStartTime(itemMessage.getStartTime());
        todoitem.setUserID(userid);
        Optional<Todoitem> item=itemService.SaveTodoItem(todoitem);
        Map<String,Object> response=new HashMap<>();
        if(item.isPresent()){
            response.put("status",HttpStatus.OK);
            response.put("date",item.get().getId());
        }
        else{
            response.put("status",HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> DeleteItem(@RequestAttribute Long userId , @RequestBody ItemMessage itemMessage) {
        int userid= Math.toIntExact(userId);
        itemService.DeleteTodoItem(userid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String,Object>> UpdateItem(@RequestAttribute Long userId , @RequestBody ItemMessage itemMessage) {
        int userid= Math.toIntExact(userId);
        Todoitem todoitem=new Todoitem();
        todoitem.setContont(itemMessage.getcontent());
        todoitem.setFinish(false);
        todoitem.setDeadline(itemMessage.getDeadline());
        todoitem.setImportance(itemMessage.getImportance());
        todoitem.setStartTime(itemMessage.getStartTime());
        todoitem.setUserID(userid);
        if(itemService.UpdateItem(todoitem).isPresent())
            return ResponseEntity.status(HttpStatus.OK).build();
        else
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

}
