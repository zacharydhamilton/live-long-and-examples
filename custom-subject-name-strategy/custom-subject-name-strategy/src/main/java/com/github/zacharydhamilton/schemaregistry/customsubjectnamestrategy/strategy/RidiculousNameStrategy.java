package com.github.zacharydhamilton.schemaregistry.customsubjectnamestrategy.strategy;

import java.util.ArrayList;
import java.util.Map;

import io.confluent.kafka.schemaregistry.ParsedSchema;
import io.confluent.kafka.serializers.subject.strategy.SubjectNameStrategy;

public class RidiculousNameStrategy implements SubjectNameStrategy {
    private String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private String crazy    = "48cd3f9h1jk1mn0p9r57uvwxyz";

    @Override
    public void configure(Map<String, ?> config) {}

    @Override
    public String subjectName(String topic, boolean isKey, ParsedSchema schema) {
        String subject = "";
        for (char c : topic.toCharArray()) {
            int i = alphabet.indexOf(c);
            subject = subject + crazy.charAt(i);
        }
        subject = subject + "-";
        for (char c : (isKey ? "key" : "value").toCharArray()) {
            int i = alphabet.indexOf(c);
            subject = subject + crazy.charAt(i);
        }
        return subject;
    }
}
