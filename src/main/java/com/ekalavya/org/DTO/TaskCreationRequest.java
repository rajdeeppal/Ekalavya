package com.ekalavya.org.DTO;

import lombok.Data;

@Data
public class TaskCreationRequest {

    private String name;
    private String typeOfUnit;
    private int units;
    private Long ratePerUnit;
    private Long totalCost;
    private int beneficiaryContribution;
    private Long grantAmount;
    private int yearOfSanction;


}
