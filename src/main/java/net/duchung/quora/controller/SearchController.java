package net.duchung.quora.controller;

import net.duchung.quora.dto.response.ListAnswerResponse;
import net.duchung.quora.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;
    @GetMapping("")
    public ResponseEntity<ListAnswerResponse> searchByTag(@RequestParam String topic, @RequestParam String q,@RequestParam(defaultValue = "0" ) int page) {

        return ResponseEntity.ok(searchService.search(q,topic,page));
    }
}
