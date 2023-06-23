package com.github.zacharydhamilton.consumeroffsetresetting.commands;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.zacharydhamilton.consumeroffsetresetting.consumer.ConsumerService;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

@Component
@Command(name = "consumer", version = "1.0", mixinStandardHelpOptions = true,
    subcommands = { 
        ConsumerCommand.StartCommand.class,
        ConsumerCommand.StopCommand.class,
        ConsumerCommand.ConsumeCommand.class
    })
public class ConsumerCommand implements Callable<Void> {
    @ParentCommand RootCommand parent; 

    public Void call() {
        CommandLine.usage(this, System.out);
        return null;
    }

    @Command(name = "start", description = "Start the consumer.", version = "1.0")
    public static class StartCommand implements Callable<Void> {
        @ParentCommand ConsumerCommand parent;
        @Autowired private ConsumerService consumer;

        public Void call() throws IOException {
            consumer.start();
            return null;
        }
    }

    @Command(name = "stop", description = "Stop the consumer.", version = "1.0")
    public static class StopCommand implements Callable<Void> {
        @ParentCommand ConsumerCommand parent;
        @Autowired private ConsumerService consumer;

        public Void call() throws Exception {
            consumer.stop();
            return null;
        }
    }

    @Command(name = "consume", description = "Consume records from the topic.", version = "1.0")
    public static class ConsumeCommand implements Callable<Void> {
        @ParentCommand ConsumerCommand parent;
        @Autowired private ConsumerService consumer;

        public Void call() throws InterruptedException {
            consumer.consume();
            return null;
        }
    }
}
