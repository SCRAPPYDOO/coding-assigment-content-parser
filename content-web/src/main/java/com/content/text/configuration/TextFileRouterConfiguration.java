package com.content.text.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.Router;
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
public class TextFileRouterConfiguration {

    private static final String WAITING_TEXT_FILE_PATTERN = "*.waiting";
    private static final String FILE_POSTFIX_EXTENSION = ".inprogress";
    private static final String TXT_FILE_EXTENSION = ".txt" + FILE_POSTFIX_EXTENSION;
    private static final String PDF_FILE_EXTENSION = ".pdf" + FILE_POSTFIX_EXTENSION;
    private static final String ODT_FILE_EXTENSION = ".odt" + FILE_POSTFIX_EXTENSION;
    private static final String DOC_FILE_EXTENSION = ".doc" + FILE_POSTFIX_EXTENSION;
    private static final String LOG_FILE_EXTENSION = ".log" + FILE_POSTFIX_EXTENSION;

    private final String waitingTextFileDirectory;
    private final String inprogressTextFileDirectory;

    public TextFileRouterConfiguration(@Value("${waiting.text.directory}") final String waitingTextFileDirectory,
                                       @Value("${inprogress.text.directory}") final String inprogressTextFileDirectory) {
        this.waitingTextFileDirectory = waitingTextFileDirectory;
        this.inprogressTextFileDirectory = inprogressTextFileDirectory;
    }

    @Bean
    @InboundChannelAdapter(value = ChannelConfiguration.WAITING_TEXT_FILE_CHANNEL_NAME, poller = @Poller(fixedRate = "100", taskExecutor= "processingTextFileTaskExecutor", maxMessagesPerPoll="-1"))
    public MessageSource<File> waitingTextFileMessageSource() {
        FileReadingMessageSource sourceReader = new FileReadingMessageSource();
        sourceReader.setDirectory(new File(waitingTextFileDirectory));

        final CompositeFileListFilter compositeFileListFilter = new CompositeFileListFilter();
        compositeFileListFilter.addFilter(new SimplePatternFileListFilter(WAITING_TEXT_FILE_PATTERN));
        compositeFileListFilter.addFilter(new AcceptOnceFileListFilter(10000));
        sourceReader.setFilter(compositeFileListFilter);

        sourceReader.setAutoCreateDirectory(true);
        sourceReader.setCountsEnabled(true);
        return sourceReader;
    }

    @Bean
    @ServiceActivator(inputChannel = ChannelConfiguration.WAITING_TEXT_FILE_CHANNEL_NAME)
    public MessageHandler waitingTextFileMessageHandler(final DefaultFileNameGenerator inprogressFileNameGenerator) {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(inprogressTextFileDirectory));
        handler.setDeleteSourceFiles(true);
        handler.setAutoCreateDirectory(true);
        handler.setFileExistsMode(FileExistsMode.IGNORE);
        handler.setFileNameGenerator(inprogressFileNameGenerator);
        handler.setOutputChannelName(ChannelConfiguration.TEXT_FILE_ROUTER_CHANNEL_NAME);
        handler.setCountsEnabled(true);
        handler.setStatsEnabled(true);
        return handler;
    }


    @Router(inputChannel = ChannelConfiguration.TEXT_FILE_ROUTER_CHANNEL_NAME)
    public String textFileExtensionRouter(final File file) {
        final String filePath = file.getPath();
        if (filePath.endsWith(TXT_FILE_EXTENSION)) {
            return ChannelConfiguration.TXT_FILE_PROCESSING_CHANNEL_NAME;
        /*} else if (filePath.endsWith(PDF_FILE_EXTENSION)) {
            return ChannelConfiguration.PDF_FILE_PROCESSING_CHANNEL_NAME;
        } else if (filePath.endsWith(ODT_FILE_EXTENSION)) {
            return ChannelConfiguration.ODT_FILE_PROCESSING_CHANNEL_NAME;
        } else if (filePath.endsWith(DOC_FILE_EXTENSION)) {
            return ChannelConfiguration.DOC_FILE_PROCESSING_CHANNEL_NAME;
        } else if (filePath.endsWith(LOG_FILE_EXTENSION)) {
            return ChannelConfiguration.TXT_FILE_PROCESSING_CHANNEL_NAME;*/
        } else {
            log.info("unsuppoerted file {}", filePath);
            return ChannelConfiguration.UNSUPPORTED_TEXT_FILE_CHANNEL_NAME;
        }
    }
}
