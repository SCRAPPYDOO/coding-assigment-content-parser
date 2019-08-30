package com.content.text.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElasticSearchRepository extends ElasticsearchRepository<ElasticSearchDocument, String> {
    List<ElasticSearchDocument> findAllByContentContains(String keyword);
}
