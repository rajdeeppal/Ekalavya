package com.ekalavya.org.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="m_component")
@Getter
@Setter
@AllArgsConstructor
public class M_Component {
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

    private String isCompleted;

    public M_Component(){
        this.isCompleted = "N";
    }
}
