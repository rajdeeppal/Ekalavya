package com.ekalavya.org.DTO;


import lombok.Data;

import java.util.List;

@Data
public class ComponentResponse {
    private String name;
    private List<ActivityResponse> activities;
}
