package br.eti.rbritta.sql2sheets.scheduler;

import br.eti.rbritta.sql2sheets.service.DataSourceService;
import br.eti.rbritta.sql2sheets.exception.InvalidTaskCronException;
import br.eti.rbritta.sql2sheets.model.Task;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class JobFactory {

    private static final Logger logger = LoggerFactory.getLogger(JobFactory.class);

    @Autowired
    private DataSourceService dataSourceConfigService;

    public JobDetail jobOf(Task task) {
        return JobBuilder.newJob(TaskBean.class)
                .storeDurably()
                .withIdentity(task.getId().toString())
                .setJobData(dataOf(task))
                .build();
    }

    private JobDataMap dataOf(Task task) {
        final Map<String, Object> data = new HashMap<>();
        data.put(TaskBean.PARAM_DATASOURCE, dataSourceConfigService.createDataSource(task.getDataSourceConfig()));
        data.put(TaskBean.PARAM_TASK, task);
        return new JobDataMap(data);
    }

    public Trigger triggerOf(JobDetail job, Task task) {
        return newTrigger().withIdentity(task.getId().toString())
                .forJob(job)
                .withSchedule(cronOf(task))
                .build();
    }

    public CronScheduleBuilder cronOf(Task task) {
        try {
            return CronScheduleBuilder.cronSchedule(task.getCron());
        } catch(Exception e) {
            logger.error("Invalid cron: " + task.getCron(), e);
            throw new InvalidTaskCronException();
        }
    }

}
