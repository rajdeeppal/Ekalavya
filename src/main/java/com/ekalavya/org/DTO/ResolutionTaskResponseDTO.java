package com.ekalavya.org.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResolutionTaskResponseDTO {
    private String projectName;
    private String userName;
    private String uploadTimestamp;
    private Long documentId;
    private String documentFileName;
}
