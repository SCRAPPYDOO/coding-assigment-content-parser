package com.content.text.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
public class ChannelConfiguration {
    public static final String INPUT_TEXT_FILE_CHANNEL_NAME = "inputTextChannel";
    public static final String WAITING_TEXT_FILE_CHANNEL_NAME = "waitingTextChannel";
    public static final String TEXT_FILE_ROUTER_CHANNEL_NAME = "textFileRouterChannel";

    public static final String TXT_FILE_PROCESSING_CHANNEL_NAME = "txtFileProcessingChannelName";
    public static final String PDF_FILE_PROCESSING_CHANNEL_NAME = "pdfFileProcessingChannelName";
    public static final String ODT_FILE_PROCESSING_CHANNEL_NAME = "odtFileProcessingChannelName";
    public static final String DOC_FILE_PROCESSING_CHANNEL_NAME = "docFileProcessingChannelName";

    public static final String JOB_GATEWAY_CHANNEL_NAME = "jobGatewayChannelName";

    public static final String COMPLETED_TEXT_FILE_CHANNEL_NAME = "completedTextChannel";

    public static final String UNSUPPORTED_TEXT_FILE_CHANNEL_NAME = "unsupportedTextChannel";

    @Bean
    public MessageChannel inputTextChannel() {
        return new QueueChannel();
    }

    @Bean
    public MessageChannel waitingTextChannel() {
        return new QueueChannel();
    }


    @Bean
    public MessageChannel textFileRouterChannel() {
        return new QueueChannel();
    }

    @Bean
    public MessageChannel txtFileProcessingChannelName() {
        return new QueueChannel();
    }
    @Bean
    public MessageChannel pdfFileProcessingChannelName() {
        return new QueueChannel();
    }
    @Bean
    public MessageChannel odtFileProcessingChannelName() {
        return new QueueChannel();
    }
    @Bean
    public MessageChannel docFileProcessingChannelName() {
        return new QueueChannel();
    }

    @Bean
    public MessageChannel completedTextChannel() {
        return new QueueChannel();
    }

    @Bean
    public MessageChannel jobGatewayChannelName() {
        return new QueueChannel();
    }

    @Bean
    public MessageChannel unsupportedTextChannel() {
        return new QueueChannel();
    }

}
