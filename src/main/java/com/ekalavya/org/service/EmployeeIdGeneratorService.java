package com.ekalavya.org.service;

import com.ekalavya.org.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeIdGeneratorService {

    @Autowired
    private UserRepository userRepository;


    @Transactional
    public Long generateNextEmpId() {
        Long lastEmpId = userRepository.findMaxEmpId();
        if (lastEmpId == null) {
            return 1000L;
        }
        return lastEmpId + 1;
    }
}
