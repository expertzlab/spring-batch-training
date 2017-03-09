package com.ksgbabu.batch.training;

import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.Map;

/**
 * Created by gireeshbabu on 08/03/17.
 */
public class JobDetailBean extends QuartzJobBean {

    private JobLocator jobLocator;

    private JobLauncher jobLauncher;

    public void setJobLocator(JobLocator jobLocator) {
        this.jobLocator = jobLocator;
    }

    public void setJobLauncher(JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
    }

    protected void executeInternal(org.quartz.JobExecutionContext context) {
        Map<String,Object> jobDataMap = context.getMergedJobDataMap();
        String jobName = (String) jobDataMap.get("jobName");
        JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);

        try {
            jobLauncher.run(jobLocator.getJob(jobName), jobParameters);
        } catch (JobExecutionException e) {
            e.printStackTrace();
        }
    }

    //get params from jobDataAsMap property, job-quartz.xml
    private JobParameters getJobParametersFromJobMap(Map<String, Object> jobDataMap) {

        JobParametersBuilder builder = new JobParametersBuilder();

        for (Map.Entry<String, Object> entry : jobDataMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String && !key.equals("jobName")) {
                builder.addString(key, (String) value);
            } else if (value instanceof Float || value instanceof Double) {
                builder.addDouble(key, ((Number) value).doubleValue());
            } else if (value instanceof Integer || value instanceof Long) {
                builder.addLong(key, ((Number) value).longValue());
            } else if (value instanceof Date) {
                builder.addDate(key, (Date) value);
            } else {
                // JobDataMap contains values which are not job parameters
                // (ignoring)
            }
        }
        //need unique job parameter to rerun the completed job
        builder.addDate("run date", new Date());

        return builder.toJobParameters();

    }
}
