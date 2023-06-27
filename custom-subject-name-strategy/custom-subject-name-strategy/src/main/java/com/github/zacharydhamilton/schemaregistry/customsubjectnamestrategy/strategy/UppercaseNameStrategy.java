package com.github.zacharydhamilton.schemaregistry.customsubjectnamestrategy.strategy;

import java.util.Map;

import io.confluent.kafka.schemaregistry.ParsedSchema;
import io.confluent.kafka.serializers.subject.strategy.SubjectNameStrategy;

public class UppercaseNameStrategy implements SubjectNameStrategy {
    @Override
    public void configure(Map<String, ?> config) {}

    @Override
    public String subjectName(String topic, boolean isKey, ParsedSchema schema) {
        if (schema == null) {
            return null;
        } else {
            return topic.toUpperCase()+(isKey ? "-KEY" : "-VALUE");
        }
    }
}
