package com.github.madhav.uploader.controller;

import com.github.madhav.uploader.model.FileData;
import com.github.madhav.uploader.service.FileService;
import com.github.madhav.uploader.service.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/file")
public class FileController {

    @Value("${status.server}")
    private String statusServerURL;

    private final FileService fileService;
    private final KafkaProducer kafkaProducer;

    @Autowired
    public FileController(FileService fileService, KafkaProducer kafkaProducer) {
        this.fileService = fileService;
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping
    public String index() {
        return "home";
    }

    @PostMapping("/upload")
    @CrossOrigin
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("width") Integer width,
                             @RequestParam("height") Integer height, RedirectAttributes redirectAttributes) {

        try {
            String fileName = fileService.save(file);
            kafkaProducer.sendMessage(new FileData(fileName, width, height));
            return "redirect:" + statusServerURL + "/" + fileName;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error in uploading " + file.getOriginalFilename() + "!");
            return "redirect:/file";
        }
    }

    @GetMapping("{filename}")
    @ResponseBody
    public String getFile(@PathVariable String filename, Model model) {

        model.addAttribute("filename", filename);
        return "status";
    }

}