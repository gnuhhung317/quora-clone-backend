package net.duchung.quora.controller;

import net.duchung.quora.data.response.AnswerResponse;
import net.duchung.quora.data.response.ListAnswerResponse;
import net.duchung.quora.service.ElasticSearchService;
import net.duchung.quora.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("${api.base.url}/search")
public class SearchController {

    @Autowired
    private SearchService searchService;
    @Autowired
    private ElasticSearchService elasticSearchService;
    @GetMapping("")
    public ResponseEntity<List<AnswerResponse>> searchByTag(@RequestParam(required = false) String topic, @RequestParam(required = false) String q, @RequestParam(defaultValue = "0" ) int page) throws IOException {

        if(q==null){
            q="";
        }
        return ResponseEntity.ok(elasticSearchService.searchByContent(q));
    }
}
