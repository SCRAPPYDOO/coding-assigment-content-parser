package com.content.text.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.context.IntegrationContextUtils;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class TaskExecutorConfiguration {


    @Bean(IntegrationContextUtils.TASK_SCHEDULER_BEAN_NAME)
    public TaskScheduler inputFileTaskScheduler(@Value("${main-task-scheduler.pool-size}") final int mainPoolSize) {
        final ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(mainPoolSize);
        threadPoolTaskScheduler.setThreadNamePrefix("MainTaskScheduler-");
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);

        return threadPoolTaskScheduler;
    }

    @Bean
    public TaskExecutor inputTextFileTaskExecutor(@Value("${input.task-executor.core-pool-size}") final int inputTextCorePoolSize,
                                                  @Value("${input.task-executor.max-pool-size}") final int inputTextMaxPoolSize,
                                                  @Value("${input.task-executor.queue-capacity}") final int inputTextQueueCapacity) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(inputTextCorePoolSize);
        executor.setMaxPoolSize(inputTextMaxPoolSize);
        executor.setQueueCapacity(inputTextQueueCapacity);
        executor.setThreadNamePrefix("InputTextFileExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean
    public TaskExecutor processingTextFileTaskExecutor(@Value("${processing.task-executor.core-pool-size}") final int processTextCorePoolSize,
                                                    @Value("${processing.task-executor.max-pool-size}") final int processTextMaxPoolSize,
                                                    @Value("${processing.task-executor.queue-capacity}") final int procefssTextQueueCapacity) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(processTextCorePoolSize);
        executor.setMaxPoolSize(processTextMaxPoolSize);
        executor.setQueueCapacity(procefssTextQueueCapacity);
        executor.setThreadNamePrefix("WaitingTextFileExecutor-");
        executor.initialize();
        return executor;
    }
}
