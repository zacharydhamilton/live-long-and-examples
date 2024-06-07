package com.github.zacharydhamilton.producer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.github.zacharydhamilton.objects.Pbj;

import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializerConfig;
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer;
import io.confluent.kafka.serializers.subject.TopicNameStrategy;

public class PBJProducer {
    private KafkaProducer<String, Pbj> producer;

    public PBJProducer() throws IOException {
        Properties props = new Properties();
        addPropsFromFile(props, "client.properties");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSchemaSerializer.class.getName());
        props.put(KafkaJsonSchemaDeserializerConfig.AUTO_REGISTER_SCHEMAS, true);
        props.put(KafkaJsonSchemaDeserializerConfig.VALUE_SUBJECT_NAME_STRATEGY, TopicNameStrategy.class.getName());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "json-reference-schema-producer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        producer = new KafkaProducer<String, Pbj>(props);
    }

    public void produce(Pbj sandwhich) {
        ProducerRecord<String, Pbj> record = new ProducerRecord<String, Pbj>("pbj-sandwhiches", UUID.randomUUID().toString(), sandwhich);
        producer.send(record);
    }

    public void flush() {
        producer.flush();
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
