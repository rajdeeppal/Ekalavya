package com.ekalavya.org.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "m_task_update", indexes = {@Index(name = "idx_m_task_up_isCompleted", columnList = "isCompleted")})
public class M_Task_Update {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String createdDate;

    private int achievementUnit;
    private String pendingWith;
    private String isCompleted;
    private String payeeName;
    private Long currentCost;
    private int currentBeneficiaryContribution;
    private String remarks;
    private boolean isRejectionOccurred;
    private String transactionId;
    private String accountNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passbook_doc_id", referencedColumnName = "id")
    private Document passbookDoc;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_update_id")
    private List<Document> otherDocs;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonBackReference
    private M_Task task;

    public M_Task_Update() {
        this.createdDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.isCompleted = "N";
        this.pendingWith = "PM";
        this.isRejectionOccurred = false;
        this.remarks = null;
        this.transactionId = null;
    }
}
