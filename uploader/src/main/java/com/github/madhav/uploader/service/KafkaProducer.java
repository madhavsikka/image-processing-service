package com.github.madhav.uploader.service;

import com.github.madhav.uploader.model.FileData;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "files";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(FileData message) {
        logger.info(String.format("#### -> Producing image to process -> %s", message.getFilename()));
        this.kafkaTemplate.send(TOPIC, message);
    }

}
