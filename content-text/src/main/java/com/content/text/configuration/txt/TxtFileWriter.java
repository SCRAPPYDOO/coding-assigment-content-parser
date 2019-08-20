package com.content.text.configuration.txt;

import com.content.text.elasticsearch.ElasticSearchDocument;
import com.content.text.elasticsearch.ElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class TxtFileWriter implements ItemWriter {

    private final String filePath;

    private final ElasticSearchService elasticSearchService;

    public TxtFileWriter(String filePath, ElasticSearchService elasticSearchService) {
        this.filePath = filePath;
        this.elasticSearchService = elasticSearchService;
    }

    @Override
    public void write(List items) throws Exception {
        final ElasticSearchDocument elasticSearchDocument = new ElasticSearchDocument();

        elasticSearchDocument.setId(filePath);
        elasticSearchDocument.setFilePath(filePath);
        elasticSearchDocument.setContent(items);

        log.info("writing file content into elastic search for document {}", filePath);

        elasticSearchService.save(elasticSearchDocument);
    }
}
