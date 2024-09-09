package com.ekalavya.org.DTO;

import com.ekalavya.org.entity.AddressDetails;
import jakarta.persistence.Embedded;
import lombok.Data;

import java.util.List;

@Data
public class BeneficiaryResponse {
    private String name;
    private String guardianName;
    private String villageName;
    private String mandalName;
    private String districtName;
    private String stateName;

    private Long aadharNumber;
    private List<ComponentResponse> components;
}
