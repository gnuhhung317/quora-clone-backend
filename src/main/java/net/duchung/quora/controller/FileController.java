package net.duchung.quora.controller;

import com.cloudinary.utils.ObjectUtils;
import net.duchung.quora.config.CloudinaryConfig;
import net.duchung.quora.service.impl.CloudinaryService;
import net.duchung.quora.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("${api.base.url}/files")
public class FileController {
    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    public ResponseEntity<Map> uploadFile(@RequestParam("type") String type, @RequestBody MultipartFile  file) {
        if(type.equals("image")) {
            return ResponseEntity.ok(cloudinaryService.uploadImage(file));
        } else if (type.equals("video")) {
            return ResponseEntity.ok(cloudinaryService.uploadVideo(file));
        }else {
            return ResponseEntity.ok(ObjectUtils.emptyMap());
        }
    }

}
