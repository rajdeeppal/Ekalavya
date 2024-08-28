package com.ekalavya.org.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String componentName;

    @ManyToOne
    @JoinColumn(name = "vertical_id")
    @JsonBackReference
    private Vertical vertical;

    @OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Activity> activities;
}
