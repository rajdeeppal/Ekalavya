package com.ekalavya.org.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="m_task")
public class M_Task {
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

    public String getTypeOfUnit() {
        return typeOfUnit;
    }

    public void setTypeOfUnit(String typeOfUnit) {
        this.typeOfUnit = typeOfUnit;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getRatePerUnit() {
        return ratePerUnit;
    }

    public void setRatePerUnit(String ratePerUnit) {
        this.ratePerUnit = ratePerUnit;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public int getBeneficiaryContribution() {
        return beneficiaryContribution;
    }

    public void setBeneficiaryContribution(int beneficiaryContribution) {
        this.beneficiaryContribution = beneficiaryContribution;
    }

    public int getGrantAmount() {
        return grantAmount;
    }

    public void setGrantAmount(int grantAmount) {
        this.grantAmount = grantAmount;
    }

    public int getYearOfSanction() {
        return yearOfSanction;
    }

    public void setYearOfSanction(int yearOfSanction) {
        this.yearOfSanction = yearOfSanction;
    }

    public M_Activity getActivity() {
        return activity;
    }

    public void setActivity(M_Activity activity) {
        this.activity = activity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String typeOfUnit;
    private int units;
    private String ratePerUnit;
    private int totalCost;
    private int beneficiaryContribution;
    private int grantAmount;
    private int yearOfSanction;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    @JsonBackReference
    private M_Activity activity;
}
