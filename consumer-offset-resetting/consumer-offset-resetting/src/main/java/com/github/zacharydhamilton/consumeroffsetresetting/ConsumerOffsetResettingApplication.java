package com.github.zacharydhamilton.consumeroffsetresetting;

import java.io.IOException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ImportResource;

import com.github.zacharydhamilton.consumeroffsetresetting.commands.RootCommand;

import jline.console.ConsoleReader;
import jline.console.completer.ArgumentCompleter.ArgumentList;
import jline.console.completer.ArgumentCompleter.WhitespaceArgumentDelimiter;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;
import picocli.shell.jline2.PicocliJLineCompleter;

@SpringBootApplication
@ImportResource("classpath:beans.xml")
public class ConsumerOffsetResettingApplication implements CommandLineRunner {
	private IFactory factory;
	private ConsoleReader reader;
	private RootCommand root;

	public ConsumerOffsetResettingApplication(IFactory factory, ConsoleReader reader, RootCommand root) {
		this.factory = factory;
		this.reader = reader;
		this.root = root;
	}

	public void run(String... args) throws IOException {
		CommandLine cli = new CommandLine(root, factory);
		reader.addCompleter(new PicocliJLineCompleter(cli.getCommandSpec()));

		String line;
		while ((line = reader.readLine("command> ")) != null) {
			ArgumentList list = new WhitespaceArgumentDelimiter().delimit(line, line.length());
			new CommandLine(root, factory).execute(list.getArguments());
		}
	}

	public static void main(String[] args) throws IOException {
		new SpringApplicationBuilder(ConsumerOffsetResettingApplication.class).logStartupInfo(false).build().run(args);	
	}
}
