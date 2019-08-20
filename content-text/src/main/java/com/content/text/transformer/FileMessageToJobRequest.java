package com.content.text.transformer;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;

import java.io.File;
import java.util.Date;

public class FileMessageToJobRequest {

    private static  final String FILE_PARAMETER_NAME = "input.file.name";
    private static  final String DATE_PARAMETER_NAME = "input.file.date";
    private final Job job;

    public FileMessageToJobRequest(Job job) {
        this.job = job;
    }

    @Transformer
    public JobLaunchRequest toRequest(final Message<File> message) {
        final JobParameters jobParameters = new JobParametersBuilder()
                .addString(FILE_PARAMETER_NAME, message.getPayload().getAbsolutePath())
                .addDate(DATE_PARAMETER_NAME, new Date())
                .toJobParameters();

        return new JobLaunchRequest(job, jobParameters);
    }
}
