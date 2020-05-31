package br.eti.rbritta.sql2sheets;

import br.eti.rbritta.sql2sheets.model.DataSourceStatus;
import br.eti.rbritta.sql2sheets.model.TaskStatus;
import br.eti.rbritta.sql2sheets.repository.DataSourceConfigRepository;
import br.eti.rbritta.sql2sheets.repository.TaskRepository;
import br.eti.rbritta.sql2sheets.scheduler.TaskScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static br.eti.rbritta.sql2sheets.model.TaskStatus.UNDEFINED;

@Component
class ApplicationInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationInitializer.class);

    @Autowired
    private DataSourceConfigRepository dsConfigRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskScheduler taskScheduler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Application Initialized");
        taskRepository.findAll().forEach(task -> {
            try {
                task.setStatus(TaskStatus.UNDEFINED);
                taskRepository.save(task);
                taskScheduler.schedule(task);
            } catch (Exception e) {
                logger.error("Could not schedule loaded task", e);
            }
        });
        dsConfigRepository.findAll().forEach(ds -> {
            ds.setStatus(DataSourceStatus.UNDEFINED);
            dsConfigRepository.save(ds);
        });
    }
}
