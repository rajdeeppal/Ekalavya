package com.ekalavya.org.repository;

import com.ekalavya.org.DTO.PaymentDTO;
import com.ekalavya.org.entity.M_Task_Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MTaskUpdateRepository extends JpaRepository<M_Task_Update, Long> {

    @Query("SELECT new com.ekalavya.org.DTO.PaymentDTO(tu.payeeName, tu.accountNumber, "
            + "new com.ekalavya.org.DTO.TaskAmountDTO(tu.task.taskName, SUM(tu.currentCost)), SUM(tu.currentCost)) "
            + "FROM M_Task_Update tu WHERE tu.isCompleted = 'N' AND tu.pendingWith = 'AO' AND tu.isRejectionOccurred = false "
            + "GROUP BY tu.payeeName, tu.accountNumber, tu.task.taskName")
    List<PaymentDTO> findPendingPaymentsGroupedByPayeeAndTask();

    @Modifying
    @Query("UPDATE M_Task_Update tu SET tu.isCompleted = :paymentStatus "
            + "WHERE tu.payeeName = :payeeName AND tu.accountNumber = :accountNumber AND tu.isCompleted = 'N' "
            + "AND tu.pendingWith = 'AO' AND tu.isRejectionOccurred = false ")
    void updatePaymentStatusForPayee(@Param("payeeName") String payeeName,
                                     @Param("accountNumber") String accountNumber,
                                     @Param("paymentStatus") String paymentStatus);
}
