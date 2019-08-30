package com.content.text.configuration.unsupported;

import com.content.text.configuration.ChannelConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageHandler;

import java.io.File;

@Configuration
public class UnsuppoertedTextFileChannelConfiguration {

    private final String unsuppoertedTextFileDirectory;

    public UnsuppoertedTextFileChannelConfiguration(@Value("${text.file.unsuppoerted.directory}") final String unsuppoertedTextFileDirectory) {
        this.unsuppoertedTextFileDirectory = unsuppoertedTextFileDirectory;
    }

    @Bean
    @ServiceActivator(inputChannel = ChannelConfiguration.UNSUPPORTED_TEXT_FILE_CHANNEL_NAME)
    public MessageHandler unsuppoertedTextFileMessageHandler() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(unsuppoertedTextFileDirectory));
        handler.setDeleteSourceFiles(true);
        handler.setAutoCreateDirectory(true);
        handler.setFileExistsMode(FileExistsMode.REPLACE);
        handler.setExpectReply(false);
        return handler;
    }
}
