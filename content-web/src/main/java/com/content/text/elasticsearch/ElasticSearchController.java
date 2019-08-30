package com.content.text.elasticsearch;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ElasticSearchController {

    private final ElasticSearchService elasticSearchService;

    public ElasticSearchController(ElasticSearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @GetMapping("/search/{keyword}")
    public List<ElasticSearchDocument> findAllByKeyword(@PathVariable final String keyword) {
        return elasticSearchService.findAllByKeyword(keyword);
    }
}
