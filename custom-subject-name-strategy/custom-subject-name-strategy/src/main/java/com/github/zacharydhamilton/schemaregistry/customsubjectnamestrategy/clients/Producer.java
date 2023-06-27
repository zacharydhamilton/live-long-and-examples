package com.github.zacharydhamilton.schemaregistry.customsubjectnamestrategy.clients;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import com.github.zacharydhamilton.events.Fruit;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.serializers.subject.strategy.SubjectNameStrategy;

public class Producer {
    private String CLIENT_ID = "-producer";
    private String TOPIC_NAME = "fruits";
    private String NAME;
    private KafkaProducer<String, GenericRecord> producer;
    private Class<?> strategy;

    public Producer(String name, Class<?> subjectNameStrategy) throws IOException {
        NAME = name;
        strategy = subjectNameStrategy;
        Properties props = new Properties();
		addPropsFromFile(props, "client.properties");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, true);
        props.put(KafkaAvroSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY, subjectNameStrategy.getName());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, NAME+CLIENT_ID);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        producer = new KafkaProducer<String, GenericRecord>(props);
    }

    public void send(Fruit fruit) throws InterruptedException, ExecutionException {
        ProducerRecord<String, GenericRecord> record = new ProducerRecord<String, GenericRecord>(TOPIC_NAME, UUID.randomUUID().toString(), fruit);
        Future<RecordMetadata> futureRecordMetadata = producer.send(record);
        RecordMetadata recordMetadata = futureRecordMetadata.get();
        System.out.println(String.format("Produced '%s' with '%s' to topic '%s', partition '%s'", fruit.getClass().getSimpleName(), strategy.getSimpleName(), TOPIC_NAME, recordMetadata.partition()));
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
