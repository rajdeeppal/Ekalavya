package com.ekalavya.org.DTO;

import lombok.Data;

@Data
public class TaskCreationRequest {

    private String name;
    private String typeOfUnit;
    private int units;
    private String ratePerUnit;
    private int totalCost;
    private int beneficiaryContribution;
    private int grantAmount;
    private int yearOfSanction;


}
