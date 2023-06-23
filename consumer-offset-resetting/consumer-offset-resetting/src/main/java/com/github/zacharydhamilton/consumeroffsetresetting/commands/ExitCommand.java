package com.github.zacharydhamilton.consumeroffsetresetting.commands;

import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

@Component
@Command(name = "exit", description = "Exit the shell", version = "1.0")
public class ExitCommand implements Callable<Void> {
    @ParentCommand RootCommand parent;

    public Void call() {
        System.exit(0);
        return null;
    }
}
