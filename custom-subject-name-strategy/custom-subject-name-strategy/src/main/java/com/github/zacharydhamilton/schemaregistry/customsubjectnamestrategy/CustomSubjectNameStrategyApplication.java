package com.github.zacharydhamilton.schemaregistry.customsubjectnamestrategy;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.zacharydhamilton.events.Fruit;
import com.github.zacharydhamilton.schemaregistry.customsubjectnamestrategy.clients.Producer;
import com.github.zacharydhamilton.schemaregistry.customsubjectnamestrategy.strategy.RidiculousNameStrategy;
import com.github.zacharydhamilton.schemaregistry.customsubjectnamestrategy.strategy.UppercaseNameStrategy;

@SpringBootApplication
public class CustomSubjectNameStrategyApplication {

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		SpringApplication.run(CustomSubjectNameStrategyApplication.class, args);

		Producer upperCaseProducer = new Producer("uppercase-strat", UppercaseNameStrategy.class);
		upperCaseProducer.send(new Fruit("strawberry", "red", true));

		Producer ridiculousProducer = new Producer("ridiculous-strat", RidiculousNameStrategy.class);
		ridiculousProducer.send(new Fruit("apple", "green", true));
	}

}
