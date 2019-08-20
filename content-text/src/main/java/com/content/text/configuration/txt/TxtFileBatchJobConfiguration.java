package com.content.text.configuration.txt;

import com.content.text.elasticsearch.ElasticSearchService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class TxtFileBatchJobConfiguration {

    private static final String TXT_FILE_JOB_NAME = "text-file-parse-job";

    private static final String TXT_FILE_JOB_PROCESSING_FILE = "text-file-parse-job-processing-file";

    private static final String TXT_FILE_READER_NAME = "text-file-reader";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ResourceLoader resourceLoader;

    public TxtFileBatchJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, ResourceLoader resourceLoader) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public Job txtFileJob(final Step txtFileParseJob, final Step renameToInProgress, final Step renameToInCompleted) {
        return this.jobBuilderFactory
                .get(TXT_FILE_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                //.start(renameToInProgress)
                .start(txtFileParseJob)
                .next(renameToInCompleted)
                .build();
    }

    @Bean
    @StepScope
    public TxtFileWriter txtFileWriter(@Value("#{jobParameters['input.file.name']}") final String textFilePath, final ElasticSearchService elasticSearchService) {
        return new TxtFileWriter(textFilePath, elasticSearchService);
    }

    @Bean
    public Step txtFileParseJob(final TxtFileWriter txtFileWriter, final TxtFileProcessor txtFileProcessor) {
        return this.stepBuilderFactory
                .<String, String>get(TXT_FILE_JOB_PROCESSING_FILE)
                .chunk(1000)
                .reader(txtFileReader(null))
                .processor(txtFileProcessor)
                .writer(txtFileWriter)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<String> txtFileReader(@Value("#{jobParameters['input.file.name']}") final String textFilePath) {
        return new FlatFileItemReaderBuilder<String>()
                .strict(false)
                .name(TXT_FILE_READER_NAME)
                .resource(resourceLoader.getResource(String.format("file:%s", textFilePath)))
                .lineMapper((String line, int lineNumber) -> line)
                .build();
    }
}
