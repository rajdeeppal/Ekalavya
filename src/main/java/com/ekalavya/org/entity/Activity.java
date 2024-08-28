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
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String activityName;

    @ManyToOne
    @JoinColumn(name = "component_id")
    @JsonBackReference
    private Component component;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Task> tasks;
}
