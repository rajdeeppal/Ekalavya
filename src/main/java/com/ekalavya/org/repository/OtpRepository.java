package com.ekalavya.org.repository;

import com.ekalavya.org.entity.OtpDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepository extends JpaRepository<OtpDetails, String> {

    OtpDetails findByUsername(String username);
}
