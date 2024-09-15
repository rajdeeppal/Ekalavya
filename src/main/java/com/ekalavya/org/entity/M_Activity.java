package com.ekalavya.org.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "m_activity")
@Getter
@Setter
@AllArgsConstructor
public class M_Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "component_id", nullable = false)
    private M_Component component;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<M_Task> tasks = new HashSet<>();

    private String isCompleted;

    public M_Activity(){
        this.isCompleted = "N";
    }
}
