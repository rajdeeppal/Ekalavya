package com.ekalavya.org.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "m_beneficiary")
@Getter
@Setter
@AllArgsConstructor
public class M_Beneficiary {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String guardianName;
//    @Embedded
//    private AddressDetails addressDetails;

    private String stateName;

    private String villageName;

    private String mandalName;
    private String districtName;

    private Long aadharNumber;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "beneficiary", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<M_Component> components = new HashSet<>();

    private String terminate;

    public M_Beneficiary(){
        this.terminate = "N";
    }
}
