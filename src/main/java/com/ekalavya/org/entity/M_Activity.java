package com.ekalavya.org.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "m_activity")
public class M_Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public M_Component getComponent() {
        return component;
    }

    public void setComponent(M_Component component) {
        this.component = component;
    }

    public Set<M_Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<M_Task> tasks) {
        this.tasks = tasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String name;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "component_id", nullable = false)
    private M_Component component;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<M_Task> tasks = new HashSet<>();
}
