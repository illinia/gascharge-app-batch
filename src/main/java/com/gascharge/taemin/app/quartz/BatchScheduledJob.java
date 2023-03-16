package com.gascharge.taemin.app.quartz;

import com.gascharge.taemin.app.config.ChargeInfoJobConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class BatchScheduledJob extends QuartzJobBean {
    private final JobExplorer jobExplorer;
    private final JobLauncher jobLauncher;
    private final ChargeInfoJobConfig chargeInfoJobConfig;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
                .getNextJobParameters(chargeInfoJobConfig.job())
                .addDate("date", new Date())
                .toJobParameters();

        try {
            log.info("BatchScheduledJob 실행");
            jobLauncher.run(chargeInfoJobConfig.job(), jobParameters);
        } catch (Exception e) {
            log.error("executeInternal error", e);
            throw new RuntimeException(e);
        }
    }
}
