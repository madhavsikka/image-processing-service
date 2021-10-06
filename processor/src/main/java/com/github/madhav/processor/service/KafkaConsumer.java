package com.github.madhav.processor.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KafkaConsumer {

    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    private static final String CONSUMER_TOPIC = "files";
    private static final String PRODUCER_TOPIC = "processed_files";
    private final ProcessingService processingService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaConsumer(ProcessingService processingService) {
        this.processingService = processingService;
    }

    @KafkaListener(topics = CONSUMER_TOPIC, groupId = "file-consumer")
    public void consume(String message) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(message);
        String filename = rootNode.get("filename").asText();
        Integer width = rootNode.get("width").asInt();
        Integer height = rootNode.get("height").asInt();
        Integer quality = rootNode.get("quality").asInt();

        logger.info(String.format("#### -> Consuming image to process -> %s", filename));
        processingService.resizeImage(filename, width, height);
        logger.info(String.format("#### -> Image processed -> %s", filename));
        this.kafkaTemplate.send(PRODUCER_TOPIC, filename);
    }
}