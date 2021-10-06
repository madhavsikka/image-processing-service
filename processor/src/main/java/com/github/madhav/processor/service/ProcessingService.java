package com.github.madhav.processor.service;

import com.github.madhav.processor.model.FileData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ProcessingService {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${output.path}")
    private String outputPath;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(outputPath));
        } catch (IOException e) {
            throw new RuntimeException("Unable to create output folder" + e.getMessage());
        }
    }

    public void resizeImage(String filename, Integer width, Integer height) throws IOException {

        BufferedImage originalImage = ImageIO.read(new File(uploadPath + "/" + filename));
        Image resultingImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "jpg":
                ImageIO.write(outputImage, "jpg", new File(outputPath + "/" + filename));
                break;
            case "jpeg":
                ImageIO.write(outputImage, "jpeg", new File(outputPath + "/" + filename));
                break;
            case "png":
                ImageIO.write(outputImage, "png", new File(outputPath + "/" + filename));
                break;
            default:
                System.out.println("Extension not supported !");
        }
    }

}

