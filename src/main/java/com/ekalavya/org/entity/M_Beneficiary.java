package com.ekalavya.org.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "m_beneficiary")
public class M_Beneficiary {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public Long getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(Long aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public Set<M_Component> getComponents() {
        return components;
    }

    public void setComponents(Set<M_Component> components) {
        this.components = components;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String guardianName;
//    @Embedded
//    private AddressDetails addressDetails;

    private String stateName;

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    private String villageName;

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getMandalName() {
        return mandalName;
    }

    public void setMandalName(String mandalName) {
        this.mandalName = mandalName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    private String mandalName;
    private String districtName;

    private Long aadharNumber;

    private Long projectId;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @OneToMany(mappedBy = "beneficiary", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<M_Component> components = new HashSet<>();

}
