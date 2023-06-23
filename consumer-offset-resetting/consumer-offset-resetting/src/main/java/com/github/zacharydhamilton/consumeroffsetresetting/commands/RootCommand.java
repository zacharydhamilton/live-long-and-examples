package com.github.zacharydhamilton.consumeroffsetresetting.commands;

import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jline.console.ConsoleReader;
import picocli.CommandLine.Command;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

@Component
@Command(name = "", description = "Interactive 'consumer-offset-resetting' shell.", footer = { "", "Press Ctrl+D to exit." },
    subcommands = { ProduceCommand.class, ConsumerCommand.class, ClearCommand.class, ExitCommand.class })
public class RootCommand implements Runnable {
    final ConsoleReader reader;
    final PrintWriter out;
    @Spec private CommandSpec spec;

    @Autowired
    public RootCommand(ConsoleReader reader) {
        this.reader = reader;
        out = new PrintWriter(reader.getOutput());
    }
    public void run() {
        out.println(spec.commandLine().getUsageMessage());
    }
}
