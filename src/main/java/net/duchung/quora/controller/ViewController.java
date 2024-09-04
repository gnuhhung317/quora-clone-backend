package net.duchung.quora.controller;

import net.duchung.quora.data.dto.ViewDto;
import net.duchung.quora.service.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.base.url}/views")
public class ViewController {
    @Autowired
    private ViewService viewService;

    @PostMapping("")
    public ResponseEntity<String> createView(@RequestBody ViewDto viewDto) {
        viewService.logView(viewDto);
        return ResponseEntity.noContent().build();
    }
}

