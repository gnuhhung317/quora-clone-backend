package net.duchung.quora.service.impl;

import com.cloudinary.Cloudinary;
import net.duchung.quora.service.FileService;
import net.duchung.quora.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService  {
    @Autowired
    private Cloudinary cloudinary;
    public Map uploadImage(MultipartFile file){
        String folderName = Utils.IMAGE_FOLDER;
        try{
            return cloudinary.uploader().upload(file.getBytes(), Map.of("folder", folderName));

        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
    public Map uploadVideo(MultipartFile file) {
        String folderName = Utils.VIDEO_FOLDER;
        try{
            return cloudinary.uploader().upload(file.getBytes(), Map.of("folder", folderName, "resource_type", "video"));

        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
