package net.duchung.quora.controller;

import net.duchung.quora.data.dto.ViewDto;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(@RequestBody ViewDto view) {
        return "Ban la so "+ view.getAnswerId().toString();
    }
    @PostMapping("/test")
    public String test1() {
        return "hello";
    }
    @PutMapping("/test")
    public String test2() {
        return "hello";
    }
    @DeleteMapping("/test")
    public String test3() {
        return "hello";
    }
    @PatchMapping("/test")
    public String test4() {
        return "hello";
    }
}
