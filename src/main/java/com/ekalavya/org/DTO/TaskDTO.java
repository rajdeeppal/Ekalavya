package com.ekalavya.org.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskDTO {
    private Long id;
    private String taskName;
    private int units;
    private double ratePerUnit;
}
