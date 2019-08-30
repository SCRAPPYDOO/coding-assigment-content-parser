package com.content.text.metrics;

import org.springframework.integration.support.management.MessageChannelMetrics;
import org.springframework.integration.support.management.Statistics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetricsController {

    private final MessageChannelMetrics waitingTextChannel;

    public MetricsController(MessageChannelMetrics waitingTextChannel) {
        this.waitingTextChannel = waitingTextChannel;
    }

    @GetMapping("/metrics/rate")
    public Statistics getSendRate() {
        return waitingTextChannel.getSendRate();
    }

    @GetMapping("/metrics/duration")
    public Statistics getSendDuration() {
        return waitingTextChannel.getSendDuration();
    }

    @GetMapping("/metrics/error")
    public Statistics getErrorRate() {
        return waitingTextChannel.getErrorRate();
    }
}
