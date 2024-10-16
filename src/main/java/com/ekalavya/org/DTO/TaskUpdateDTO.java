package com.ekalavya.org.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class TaskUpdateDTO {
    private int achievementUnit;
    private String payeeName;
    private Long currentCost;
    private String accountNumber;
    private int benContribution;
    private String domainExpertEmpId;
}
