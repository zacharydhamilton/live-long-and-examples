package com.github.zacharydhamilton.consumeroffsetresetting.consumer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Service;

import jakarta.annotation.PreDestroy;

@Service
public class ConsumerService {
    private String CLIENT_ID = "replay-consumer";
    private String TOPIC_NAME = "replay-topic";
    private List<String> TOPICS = Arrays.asList(TOPIC_NAME);
    private Properties props;
    private KafkaConsumer<String, String> consumer;

    public ConsumerService() throws IOException, InterruptedException, ExecutionException {
        props = createProperties();
    }

    public void start() throws IOException {
        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(TOPICS);
    }

    public void stop() throws Exception {
        consumer.close();
    }

    public void consume() throws InterruptedException {
        boolean cont = true;
        while (cont) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            if (records.isEmpty()) cont = false;
            for (ConsumerRecord<String, String> record : records) {
                String message = String.format("Consumed: %s", record.value());
                System.out.println(message);
            }
        }
    }

    @PreDestroy
    private void destroy() {
        consumer.close();
    }

    private Properties createProperties() throws IOException {
        Properties props = new Properties();
		addPropsFromFile(props, "client.properties");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, CLIENT_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

	private void addPropsFromFile(Properties props, String file) throws IOException {
        if (!Files.exists(Paths.get(file))) {
            throw new IOException("Config file (" + file + ") does not exist or was not found.");
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            props.load(inputStream);
        }
    }
}
