package com.content.text.configuration;

import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Configuration
public class JobGatewayConfiguration {

    @Bean
    public IntegrationFlow jobGateway(final JobLaunchingGateway jobLaunchingGateway) {
        return IntegrationFlows.from(ChannelConfiguration.JOB_GATEWAY_CHANNEL_NAME)
                .handle(jobLaunchingGateway)
                .channel(ChannelConfiguration.COMPLETED_TEXT_FILE_CHANNEL_NAME)
                .get();
    }
}
