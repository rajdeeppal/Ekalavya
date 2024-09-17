package com.ekalavya.org.config;

import com.ekalavya.org.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private SchedulerService schedulerService;

    @Scheduled(cron = "0 0 21 * * ?")  // Every day at 9 PM
    public void scheduleActivityUpdate() {
        schedulerService.updateTaskCompletionStatus();
        schedulerService.updateActivityCompletionStatus();
        schedulerService.updateComponentCompletionStatus();
        schedulerService.updateBeneficiaryCompletionStatus();
        schedulerService.updateProjectCompletionStatus();
    }
}
