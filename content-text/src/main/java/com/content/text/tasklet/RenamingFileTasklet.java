package com.content.text.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Objects;

public class RenamingFileTasklet implements Tasklet {

    private final Resource resource;
    private final String fileOldPostfix;
    private final String fileNewPostfix;

    public RenamingFileTasklet(final String filePath, final String fileOldPostfix, final String fileNewPostfix, final ResourceLoader resourceLoader) {
        if(Objects.nonNull(filePath) && StringUtils.hasText(filePath)) {
            this.resource = resourceLoader.getResource(String.format("file:%s", filePath));
        } else {
            resource = null;
        }
        this.fileOldPostfix = fileOldPostfix;
        this.fileNewPostfix = fileNewPostfix;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        if(Objects.nonNull(resource)) {
            final File newFilePath = new File(resource.getFile().getAbsolutePath().replace(fileOldPostfix, fileNewPostfix));

            newFilePath.delete();

            resource.getFile().renameTo(newFilePath);
        }

        return RepeatStatus.FINISHED;
    }
}
