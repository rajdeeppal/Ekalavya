package com.ekalavya.org.controller;

import com.ekalavya.org.DTO.PaymentDTO;
import com.ekalavya.org.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ao/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/pending")
    public List<PaymentDTO> getPendingPayments() {
        return paymentService.getPendingPayments();
    }

    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestParam String payeeName,
                                                 @RequestParam String accountNumber,
                                                 @RequestParam String paymentStatus) {
        paymentService.processPayment(payeeName, accountNumber, paymentStatus);
        return ResponseEntity.ok("Payment processed successfully");
    }
}
