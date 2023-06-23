package com.github.zacharydhamilton.consumeroffsetresetting.commands;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.zacharydhamilton.consumeroffsetresetting.producer.ProducerService;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Component
@Command(name = "produce", description = "Produce a number of records.", version = "1.0", mixinStandardHelpOptions = true)
public class ProduceCommand implements Callable<Void> {
    @ParentCommand RootCommand parent;
    @Autowired private ProducerService producer;
    @Option(names = { "-n", "--num-records" }, description = "The number of records to send to Kafka.")
    private int num = 15;
    @Option(names = { "-d", "--delay-seconds" }, description = "The delay in seconds between messages." )
    private int delay = 1;

    public Void call() throws InterruptedException, ExecutionException {
        // Add validation so you can't make this thing go forever.
        producer.send(num, delay);
        return null;
    }
}
