package com.ekalavya.org.DTO;

import lombok.Data;

@Data
public class BeneficiaryCreationRequest {

    private String projectName;
    private String beneficiaryName;
    private String guardianName;
    private String villageName;
    private String mandalName;
    private String districtName;
    private String stateName;
    private Long aadharNumber;
    private int surveyNumber;
    private String componentName;
    private String activityName;
    private String taskName;
    private String typeOfUnit;
    private int units;
    private String ratePerUnit;
    private int totalCost;
    private int beneficiaryContribution;
    private int grantAmount;
    private int yearOfSanction;

}