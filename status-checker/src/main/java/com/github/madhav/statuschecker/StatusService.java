package com.github.madhav.statuschecker;

import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

@Service
public class StatusService {

    private final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String CONSUMER_TOPIC = "processed_files";
    private final HashSet<String> processedFiles = new HashSet<>();
    private final HashMap<String, SseEmitter> pendingClients = new HashMap<>();

    @KafkaListener(topics = CONSUMER_TOPIC, groupId = "processed-file-consumer")
    public void consume(String filename) throws IOException {
        logger.info(String.format("#### -> Consuming image to process -> %s", filename));
        processedFiles.add(filename);
        if (pendingClients.containsKey(filename)) {
            SseEmitter sseEmitter = pendingClients.get(filename);
            sendMessage(sseEmitter, filename);
            System.out.println("Pending Client Handled");
            sseEmitter.complete();
        } else {
            System.out.println("File processed. Client not present");
        }
    }

    public SseEmitter getNewSseEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitter.onCompletion(() -> emitter.complete());
        emitter.onTimeout(() -> logger.info("SseEmitter is timed out"));
        emitter.onError((ex) -> logger.info("SseEmitter got error:", ex));
        return emitter;
    }

    public void sendMessage(SseEmitter sseEmitter, String filename) throws IOException {
        String message = "/src/main/resources/static/images/" + filename;
        sseEmitter.send(message);
    }

    public void addToPending(String filename, SseEmitter sseEmitter) {
        pendingClients.put(filename, sseEmitter);
    }

    public Boolean isFileProcessed(String filename) {
        return processedFiles.contains(filename);
    }
}
