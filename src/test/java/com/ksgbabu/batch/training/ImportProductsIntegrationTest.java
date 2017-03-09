package com.ksgbabu.batch.training;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by gireeshbabu on 09/03/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-bean.xml"})
public class ImportProductsIntegrationTest {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        int initial = jdbcTemplate.queryForInt("select count(1) from product");
        jdbcTemplate.update("delete from product");
        jdbcTemplate.update("insert into product (id,name,description,price) values(?,?,?,?)",
                "PR....214","Nokia 2610 Phone","",102.23
        );
        jobLauncher.run(job, new JobParametersBuilder(
        ).addString("","").toJobParameters());
        int nbOfNewProducts = 6;
        Assert.assertEquals(
                initial+nbOfNewProducts, jdbcTemplate.queryForInt("select count(*) from product")
        );

    }

    @Test
    public void importProducts(){
        int initial = jdbcTemplate.queryForInt("select count(1) from product");
    }

}
