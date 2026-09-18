package org.example.aiplaner.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemMessage {
    private int userId;
    private String content;
    private boolean finish;
    private int importance;
    private LocalDateTime startTime;
    private LocalDateTime deadline;



    public int getUserId() {
        return userId;
    }

    public String getcontent() {
        return content;
    }

    public boolean isFinish() {
        return finish;
    }

    public int getImportance() {
        return importance;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }



    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
