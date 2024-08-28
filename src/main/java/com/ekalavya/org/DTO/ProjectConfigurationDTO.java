package com.ekalavya.org.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectConfigurationDTO {
    private String verticalName;
    private String componentName;
    private String activityName;
    private String taskName;
    private String units;
    private String ratePerUnit;
}
