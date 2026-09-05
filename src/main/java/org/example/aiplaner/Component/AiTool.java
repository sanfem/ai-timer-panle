package org.example.aiplaner.Component;

import org.example.aiplaner.Entity.Todoitem;
import org.example.aiplaner.Service.ItemService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class AiTool {
    private ItemService itemService;

    @Autowired
    public AiTool(ItemService itemService) {
        this.itemService = itemService;
    }

    @Tool(description="通过这个来获取用户的待办事项清单，这会返回一个JSON化的列表")
    public String GetItems(String ID){
        int id=Integer.parseInt(ID);
        List<Todoitem> todoitems=itemService.GetAllTodoItem(id);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(todoitems);
    }

    @Tool(description = "用这个来设置待办清单和任务，你需要先思考是否可行，将结果返回给用户，等到同意后再去设置就结果")
    public String SetItems(@ToolParam(description = "用户ID，即传入的ID") String userID,
                           @ToolParam(description ="具体内容") String Content,
                           @ToolParam(description = "开始时间,应是ISO格式") String start_time,
                           @ToolParam(description ="截至时间，应是ISO格式") String deadLine,
                           @ToolParam(description = "重要性，分为1到5五个等级") String importance)
    {
        Todoitem todoitem=new Todoitem();
        todoitem.setUserID(Integer.parseInt(userID));
        todoitem.setContont(Content);
        todoitem.setStartTime(LocalDateTime.parse(start_time));
        todoitem.setDeadline(LocalDateTime.parse(deadLine));
        todoitem.setImportance(Integer.parseInt(importance));
        itemService.SaveTodoItem(todoitem);
        return "success";
    }

    @Tool(description = "用这个来修改待办清单和任务，你需要先思考是否可行，将结果返回给用户，等到同意后再去设置就结果")
    public String ChackItems(@ToolParam(description = "用户ID，即传入的ID") String userID,
                             @ToolParam(description = "要修改的事项ID") String Id,
                             @ToolParam(description ="具体内容") String Content,
                             @ToolParam(description = "开始时间,应是ISO格式") String start_time,
                             @ToolParam(description ="截至时间，应是ISO格式") String deadLine,
                             @ToolParam(description = "重要性，分为1到5五个等级") String importance)
    {
        Todoitem todoitem=new Todoitem();
        todoitem.setUserID(Integer.parseInt(userID));
        todoitem.setId(Integer.parseInt(Id));
        todoitem.setContont(Content);
        todoitem.setStartTime(LocalDateTime.parse(start_time));
        todoitem.setDeadline(LocalDateTime.parse(deadLine));
        todoitem.setImportance(Integer.parseInt(importance));
        itemService.UpdateItem(todoitem);
        return "success";
    }

    @Tool(description = "用这个来删除待办清单和任务，你需要先思考是否可行，将结果返回给用户，等到同意后再去设置就结果")
    public String DeleteItems(@ToolParam(description = "要修改的事项ID") String Id)
    {
        itemService.DeleteTodoItem(Integer.parseInt(Id));
        return "success";
    }

    @Tool(description = "用这个工具来查看现在的时间和日期")
    public String LookDateTime(){
        LocalDateTime now=LocalDateTime.now();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return  now.format(formatter);  // 返回 "2026-09-04 18:50:24"
    }
}
