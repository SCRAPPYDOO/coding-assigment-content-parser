package com.content.text.configuration.txt;

import com.content.text.configuration.ChannelConfiguration;
import com.content.text.transformer.FileMessageToJobRequest;
import org.springframework.batch.core.Job;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Configuration
public class TxtFileChannelConfiguration {

    @Bean
    public FileMessageToJobRequest txtFileMessageToJobRequest(final Job textFileParseJob) {
        return new FileMessageToJobRequest(textFileParseJob);
    }

    @Bean
    public IntegrationFlow txtFileTransformer(final FileMessageToJobRequest txtFileMessageToJobRequest) {
        return IntegrationFlows.from(ChannelConfiguration.TXT_FILE_PROCESSING_CHANNEL_NAME)
                .transform(txtFileMessageToJobRequest)
                .channel(ChannelConfiguration.JOB_GATEWAY_CHANNEL_NAME)
                .get();
    }
}
