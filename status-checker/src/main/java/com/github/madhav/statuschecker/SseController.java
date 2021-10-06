package com.github.madhav.statuschecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Controller
@RequestMapping("")
public class SseController {

    @Value("${output.path}")
    private static String outputPath;

    private final StatusService statusService;

    @Autowired
    public SseController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping("/{filename}")
    public String getStatus(@PathVariable("filename") String filename) {
        return "index";
    }

    @GetMapping("/sse/{filename}")
    @CrossOrigin
    public @ResponseBody
    SseEmitter getEmitter(@PathVariable("filename") String filename) throws IOException {
        SseEmitter sseEmitter = statusService.getNewSseEmitter();
        if (statusService.isFileProcessed(filename)) {
            statusService.sendMessage(sseEmitter, filename);
            System.out.println("File already processed. Sending message");
            sseEmitter.complete();
        } else {
            statusService.addToPending(filename, sseEmitter);
            System.out.println("Client Pending");
        }
        return sseEmitter;
    }
}