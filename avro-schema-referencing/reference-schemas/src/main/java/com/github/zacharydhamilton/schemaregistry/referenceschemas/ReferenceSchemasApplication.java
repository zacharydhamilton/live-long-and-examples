package com.github.zacharydhamilton.schemaregistry.referenceschemas;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.zacharydhamilton.events.Jelly;
import com.github.zacharydhamilton.events.PBJ;
import com.github.zacharydhamilton.events.PeanutButter;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.serializers.subject.TopicNameStrategy;

@SpringBootApplication
public class ReferenceSchemasApplication {
	public static void main(String[] args) throws IOException, Exception {
		SpringApplication.run(ReferenceSchemasApplication.class, args);
		KafkaProducer<String, PBJ> producer = createProducer();  
		PeanutButter pb = new PeanutButter();
		pb.setSmooth(true);
		pb.setOrganic(true);
		Jelly j = new Jelly();
		j.setFlavor("grape");
		j.setOrganic(true);
		PBJ pbj = new PBJ();
		pbj.setBread("wheat");
		pbj.setPeanutbutter(pb);
		pbj.setJelly(j);
		ProducerRecord<String, PBJ> sandy = new ProducerRecord<String, PBJ>("sandwhiches", UUID.randomUUID().toString(), pbj);
		Future<RecordMetadata> futureRecordMetadata = producer.send(sandy);
		RecordMetadata metadata = futureRecordMetadata.get();
		System.out.println(String.format("---> Produced to '%s' at offset '%s' in partition '%s'.", metadata.topic(), metadata.offset(), metadata.partition()));
	}
	public static KafkaProducer<String, PBJ> createProducer() throws IOException {
		Properties props = new Properties();
		addPropsFromFile(props, "client.properties");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(KafkaAvroSerializerConfig.AUTO_REGISTER_SCHEMAS, false);
        props.put(KafkaAvroSerializerConfig.VALUE_SUBJECT_NAME_STRATEGY, TopicNameStrategy.class.getName());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "reference-schema-producer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
		return new KafkaProducer<String, PBJ>(props);
	}
	public static void addPropsFromFile(Properties props, String file) throws IOException {
        if (!Files.exists(Paths.get(file))) {
            throw new IOException("Config file (" + file + ") does not exist or was not found.");
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            props.load(inputStream);
        }
    }
}
