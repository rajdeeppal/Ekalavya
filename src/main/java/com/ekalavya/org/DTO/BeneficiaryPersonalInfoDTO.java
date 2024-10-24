package com.ekalavya.org.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BeneficiaryPersonalInfoDTO {
    private String beneficiaryName;
    private String guardianName;
    private String stateName;
    private String villageName;
    private String mandalName;
    private String districtName;
    private Long aadharNumber;
}
