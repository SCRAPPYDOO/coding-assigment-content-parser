package com.content.text.configuration;

import com.content.text.tasklet.RenamingFileTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.TaskExecutor;

import javax.sql.DataSource;

@Configuration
public class BatchJobConfiguration {

    private static final String JOB_STEP_RENAME_TO_IN_PROGRESS = "common-job-step-rename-to-inprogress";
    private static final String JOB_STEP_RENAME_TO_COMPLETED = "common-job-step-rename-to-completed";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ResourceLoader resourceLoader;

    public BatchJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, ResourceLoader resourceLoader) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public Step renameToInProgress() {
        return this.stepBuilderFactory
                .get(JOB_STEP_RENAME_TO_IN_PROGRESS)
                .tasklet(renamingToInProgressTasklet(null))
                .build();
    }

    @Bean
    public Step renameToInCompleted() {
        return this.stepBuilderFactory
                .get(JOB_STEP_RENAME_TO_COMPLETED)
                .tasklet(renamingToCompletedTasklet(null))
                .build();
    }

    @Bean
    @StepScope
    public RenamingFileTasklet renamingToCompletedTasklet(@Value("#{jobParameters['input.file.name']}") final String textFilePath) {
        return new RenamingFileTasklet(textFilePath, "inprogress", "completed", resourceLoader);
    }

    @Bean
    @StepScope
    public RenamingFileTasklet renamingToInProgressTasklet(@Value("#{jobParameters['input.file.name']}") final String textFilePath) {
        return new RenamingFileTasklet(textFilePath, "waiting", "inprogress", resourceLoader);
    }

    @Bean
    public SimpleJobLauncher asyncJobLauncher(final JobRepository jobRepository, final TaskExecutor inputTextFileTaskExecutor) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        launcher.setTaskExecutor(inputTextFileTaskExecutor);
        return launcher;
    }
}