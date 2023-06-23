package com.github.zacharydhamilton.consumeroffsetresetting.commands;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.springframework.stereotype.Component;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

@Component
@Command(name = "clear", description = "Clears the screen", version = "1.0")
public class ClearCommand implements Callable<Void> {
    @ParentCommand RootCommand parent;
    
    public Void call() throws IOException {
        parent.reader.clearScreen();
        return null;
    }
}
