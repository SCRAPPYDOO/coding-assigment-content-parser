package com.content.text.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.DefaultFileNameGenerator;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.filters.CompositeFileListFilter;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageHandler;

import java.io.File;

@Slf4j
@Configuration
public class InputTextFileConfiguration {

    private final Long inputTextFileMinimumAge;
    private final String inputTextFileDirectory;
    private final String inputTextFileExtensionPattern;

    private final String waitingTextFileDirectory;

    private final String completedTextFileDirectory;
    private final String failedTextFileDirectory;

    public InputTextFileConfiguration(@Value("${input.text.directory}") final String inputTextFileDirectory,
                                      @Value("${waiting.text.directory}") final String waitingTextFileDirectory,
                                      @Value("${completed.text.directory}") final String completedTextFileDirectory,
                                      @Value("${failed.text.directory}") final String failedTextFileDirectory,
                                      @Value("${input.text.minimum-age-in-seconds}") final Long inputTextFileMinimumAge,
                                      @Value("${input.text.extension-pattern}") final String inputTextFileExtensionPattern) {
        this.inputTextFileDirectory = inputTextFileDirectory;
        this.waitingTextFileDirectory = waitingTextFileDirectory;
        this.completedTextFileDirectory = completedTextFileDirectory;
        this.failedTextFileDirectory = failedTextFileDirectory;
        this.inputTextFileMinimumAge = inputTextFileMinimumAge;
        this.inputTextFileExtensionPattern = inputTextFileExtensionPattern;
    }

    @Bean
    @InboundChannelAdapter(value = ChannelConfiguration.INPUT_TEXT_FILE_CHANNEL_NAME, poller = @Poller(fixedRate = "100", taskExecutor= "inputTextFileTaskExecutor", maxMessagesPerPoll="-1"))
    public MessageSource<File> inputTextFileMessageSource() {
        FileReadingMessageSource sourceReader= new FileReadingMessageSource();
        sourceReader.setDirectory(new File(inputTextFileDirectory));

        final CompositeFileListFilter compositeFileListFilter = new CompositeFileListFilter();
        compositeFileListFilter.addFilter(new SimplePatternFileListFilter(inputTextFileExtensionPattern));
        compositeFileListFilter.addFilter(new AcceptOnceFileListFilter(10000));

        sourceReader.setFilter(compositeFileListFilter);
        sourceReader.setAutoCreateDirectory(true);
        return sourceReader;
    }

    @Bean
    @ServiceActivator(inputChannel = ChannelConfiguration.INPUT_TEXT_FILE_CHANNEL_NAME)
    public MessageHandler inputTextFileMessageHandler(final DefaultFileNameGenerator waitingFileNameGenerator) {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(waitingTextFileDirectory));
        handler.setDeleteSourceFiles(true);
        handler.setAutoCreateDirectory(true);
        handler.setFileExistsMode(FileExistsMode.IGNORE);
        handler.setExpectReply(false);
        handler.setFileNameGenerator(waitingFileNameGenerator);
        return handler;
    }

    @ServiceActivator(inputChannel = ChannelConfiguration.COMPLETED_TEXT_FILE_CHANNEL_NAME)
    public void finish() {

    }

}
