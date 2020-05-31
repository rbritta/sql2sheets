package br.eti.rbritta.sql2sheets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class ApplicationConfig {

    public static final String TASK_ASYNC_EXECUTOR = "taskAsyncExecutor";

    @Bean(TASK_ASYNC_EXECUTOR)
    public TaskExecutor taskAsyncExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("TaskAsync-");
        return executor;
    }
}
