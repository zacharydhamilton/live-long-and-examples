package com.github.zacharydhamilton.consumeroffsetresetting.producer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {
    private String CLIENT_ID = "mock-data-producer";
    private String TOPIC_NAME = "replay-topic";
    private KafkaProducer<String, String> producer;

    public ProducerService() throws IOException {
        producer = createProducer();
    }

    public void send(int records, int delay) throws InterruptedException, ExecutionException {
        System.out.println(String.format("Starting to produce data. This is anticipated to take %s seconds...", records*delay));
        String batch = UUID.randomUUID().toString();
        for (int i=0; i<records; i++) {
            String timestamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(Instant.ofEpochMilli(Instant.now().toEpochMilli()).atZone(ZoneOffset.UTC));
            String message = String.format("%s - Record number '%s' in batch '%s'", timestamp, i, batch);
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(TOPIC_NAME, UUID.randomUUID().toString(), message);
            Future<RecordMetadata> futureRecordMetadata = producer.send(record);
            RecordMetadata metadata = futureRecordMetadata.get();
            System.out.println(String.format("%s - Produced record '%s' to partition '%s' and offset '%s'.", timestamp, i, metadata.offset(), metadata.partition()));
            TimeUnit.SECONDS.sleep(delay);
        }
    }

    private KafkaProducer<String, String> createProducer() throws IOException {
		Properties props = new Properties();
		addPropsFromFile(props, "client.properties");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
		return new KafkaProducer<String, String>(props);
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
