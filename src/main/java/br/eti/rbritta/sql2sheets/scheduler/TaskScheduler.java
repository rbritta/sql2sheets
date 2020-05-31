package br.eti.rbritta.sql2sheets.scheduler;

import br.eti.rbritta.sql2sheets.model.Task;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class TaskScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TaskScheduler.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobFactory jobFactory;

    private Scheduler instance;

    @PostConstruct
    private void start() throws SchedulerException, IOException {
        final SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
        jobFactory.setApplicationContext(context);

        instance = new StdSchedulerFactory().getScheduler();
        instance.setJobFactory(jobFactory);
        instance.start();
    }

    public void schedule(Task task) {
        this.unschedule(task);
        if (!task.isActive()) {
            logger.info("Task {} is not Active and won't be scheduled", task.getId());
            return;
        }
        try {
            logger.info("Scheduling Task {}", task.getId());

            final JobDetail job = jobFactory.jobOf(task);
            final Trigger trigger = jobFactory.triggerOf(job, task);

            instance.addJob(job, true);
            instance.scheduleJob(trigger);
        } catch (Exception e) {
            logger.error("Cannot schedule task " + task.getId(), e);
        }
    }

    private void unschedule(Task task) {
        logger.info("Unscheduling Task " + task.getId());
        try {
            final String jobId = task.getId().toString();
            final boolean deleted = instance.deleteJob(JobKey.jobKey(jobId));
            logger.info(String.format("Job %s %s", jobId, deleted ? "successfully deleted" : "could not be deleted or it isn't scheduled."));
        } catch (Exception e) {
            logger.error("Cannot unschedule task " + task.getId(), e);
        }
    }

    public LocalDateTime getNextExecutionTime(Task task) {
        if (!task.isActive()) {
            return null;
        }
        try {
            final TriggerKey triggerKey = TriggerKey.triggerKey(task.getId().toString());
            return instance.getTrigger(triggerKey)
                    .getNextFireTime()
                    .toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (Exception e) {
            logger.error("Cannot get next execution time for task " + task.getId(), e);
            return null;
        }
    }
}
