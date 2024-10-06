package com.ekalavya.org.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PaymentDTO {
    private String payeeName;
    private String accountNumber;
    private List<TaskAmountDTO> tasks;
    private Long grandTotal;

    public PaymentDTO(String payeeName, String accountNumber, TaskAmountDTO taskAmount, Long grandTotal) {
        this.payeeName = payeeName;
        this.accountNumber = accountNumber;
        this.tasks = new ArrayList<>();
        this.tasks.add(taskAmount);
        this.grandTotal = grandTotal;
    }

    public void addTask(TaskAmountDTO taskAmount) {
        this.tasks.add(taskAmount);
    }
}
