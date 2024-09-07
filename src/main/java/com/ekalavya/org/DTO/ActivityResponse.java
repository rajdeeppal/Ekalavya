package com.ekalavya.org.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ActivityResponse {

    private String name;
    private List<TaskResponse> tasks;
}
