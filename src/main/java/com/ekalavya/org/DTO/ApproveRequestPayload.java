package com.ekalavya.org.DTO;

import lombok.Data;

@Data
public class ApproveRequestPayload {
    private Long requestId;
    private String approverComments;
}
