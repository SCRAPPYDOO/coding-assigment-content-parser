package com.content.text.elasticsearch;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElasticSearchService {

    private final ElasticSearchRepository elasticSearchRepository;

    public ElasticSearchService(ElasticSearchRepository elasticSearchRepository) {
        this.elasticSearchRepository = elasticSearchRepository;
    }

    public void save(final ElasticSearchDocument elasticSearchDocument) {
        elasticSearchRepository.save(elasticSearchDocument);
    }

    public List<ElasticSearchDocument> findAllByKeyword(String keyword) {
        return elasticSearchRepository.findAllByContentContains(keyword);
    }
}
