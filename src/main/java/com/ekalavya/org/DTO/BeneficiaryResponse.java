package com.ekalavya.org.DTO;

import com.ekalavya.org.entity.AddressDetails;
import jakarta.persistence.Embedded;
import lombok.Data;

import java.util.List;

@Data
public class BeneficiaryResponse {
    private String name;
    private String guardianName;
    @Embedded
    private AddressDetails addressDetails;

    private Long aadharNumber;
    private List<ComponentResponse> components;
}
