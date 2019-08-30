package com.content.configuration;

import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.integration.file.DefaultFileNameGenerator;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.scheduling.support.PeriodicTrigger;

@Configuration
@EnableIntegration
@EnableIntegrationManagement(
        defaultLoggingEnabled = "false",
        defaultCountsEnabled = "true",
        defaultStatsEnabled = "true")
@IntegrationComponentScan
public class BasicIntegrationConfiguration {

    @Bean
    public DefaultFileNameGenerator waitingFileNameGenerator() {
        DefaultFileNameGenerator defaultFileNameGenerator = new DefaultFileNameGenerator();
        defaultFileNameGenerator.setExpression("payload.name + '.waiting'");
        return defaultFileNameGenerator;
    }

    @Bean
    public DefaultFileNameGenerator inprogressFileNameGenerator() {
        DefaultFileNameGenerator defaultFileNameGenerator = new DefaultFileNameGenerator();
        defaultFileNameGenerator.setExpression("payload.name.replace('waiting', 'inprogress')");
        return defaultFileNameGenerator;
    }

    @Bean
    public DefaultFileNameGenerator completedFileNameGenerator() {
        DefaultFileNameGenerator defaultFileNameGenerator = new DefaultFileNameGenerator();
        defaultFileNameGenerator.setExpression("payload.name.replace('inprogress', 'completed')");
        return defaultFileNameGenerator;
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {

        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(100));
        return pollerMetadata;
    }

    @Bean
    public JobLaunchingGateway jobLaunchingGateway(final SimpleJobLauncher asyncJobLauncher) {
        return new JobLaunchingGateway(asyncJobLauncher);
    }
}
