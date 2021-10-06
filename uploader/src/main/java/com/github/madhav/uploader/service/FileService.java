package com.github.madhav.uploader.service;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    @Value("${upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadPath));
        } catch (IOException e) {
            throw new RuntimeException("Unable to create upload folder" + e.getMessage());
        }
    }

    public String save(MultipartFile file) {
        try {
            Path root = Paths.get(uploadPath);
            if (!Files.exists(root)) init();

            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

            String fileName = UUID.randomUUID().toString() + extension;
            Files.copy(file.getInputStream(), root.resolve(fileName));
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Unable to save the file: " + e.getMessage());
        }
    }

    public Resource load(String filename) {
        try {
            Path file = Paths.get(uploadPath)
                    .resolve(filename);

            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
