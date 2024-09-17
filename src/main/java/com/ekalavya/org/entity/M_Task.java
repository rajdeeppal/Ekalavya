package com.ekalavya.org.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(
        name = "m_task",
        indexes = {
                @Index(name = "idx_m_task_isCompleted", columnList = "isCompleted")
        }
)
public class M_Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private String typeOfUnit;
    private int units;
    private Long ratePerUnit;
    private Long totalCost;
    private int beneficiaryContribution;
    private Long grantAmount;
    private int yearOfSanction;
    private int unitRemain;
    private Long balanceRemaining;
    private String isCompleted;

    public M_Task() {
        this.isCompleted = "N";
    }

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    @JsonBackReference
    private M_Activity activity;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<M_Task_Update> taskUpdates = new HashSet<>();
}
