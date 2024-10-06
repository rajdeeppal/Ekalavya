package com.ekalavya.org.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class TaskAmountDTO {
    private String taskName;
    private Long totalAmount;
    public TaskAmountDTO(String taskName, Long totalAmount) {
        this.taskName = taskName;
        this.totalAmount = totalAmount;
    }
}
