package com.ekalavya.org.DTO;

import lombok.Data;

@Data
public class RejectRequestPayload {
    private Long requestId;
    private String rejectionComments;
}
