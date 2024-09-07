package com.ekalavya.org.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="m_component")
public class M_Component {
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

    public M_Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(M_Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Set<M_Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<M_Activity> activities) {
        this.activities = activities;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "beneficiary_id", nullable = false)
    private M_Beneficiary beneficiary;

    @OneToMany(mappedBy = "component", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<M_Activity> activities = new HashSet<>();

}
