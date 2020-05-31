package br.eti.rbritta.sql2sheets.scheduler;

import br.eti.rbritta.sql2sheets.model.Task;
import br.eti.rbritta.sql2sheets.service.TaskExecutorService;
import br.eti.rbritta.sql2sheets.service.TaskLogService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TaskBean extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(TaskBean.class);

    public static final String PARAM_DATASOURCE = "dataSource";
    public static final String PARAM_TASK = "task";

    @Autowired
    private TaskExecutorService executor;

    @Autowired
    private TaskLogService logService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        final JobDataMap dataMap = context.getMergedJobDataMap();
        final Task task = (Task) dataMap.get(PARAM_TASK);
        final BasicDataSource ds = (BasicDataSource) dataMap.get(PARAM_DATASOURCE);
        logger.info("Execution triggered by Cron {}", task.getCron());
        logService.log(task, "Execution triggered by Cron: " + task.getCron());
        executor.execute(task, ds);
    }

}