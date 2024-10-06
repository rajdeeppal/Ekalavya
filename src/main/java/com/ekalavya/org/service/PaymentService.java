package com.ekalavya.org.service;

import com.ekalavya.org.DTO.PaymentDTO;
import com.ekalavya.org.repository.MTaskUpdateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private MTaskUpdateRepository mTaskUpdateRepository;

    @Transactional(readOnly = true)
    public List<PaymentDTO> getPendingPayments() {
        return mTaskUpdateRepository.findPendingPaymentsGroupedByPayeeAndTask();
    }

    @Transactional
    public void processPayment(String payeeName, String accountNumber, String paymentStatus) {
//         Use the repository method to perform the bulk update
//        allowed value for paymentStatus = N / Y
        mTaskUpdateRepository.updatePaymentStatusForPayee(payeeName, accountNumber, paymentStatus);
    }
}
