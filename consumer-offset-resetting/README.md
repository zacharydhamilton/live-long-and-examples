# Consumer offset resetting

Resetting consumer offsets is something that it's useful to be aware of and know how to do. The following goes through using the `kafka-consumer-groups` tool that is bundled with Kafka, showcasing how it can be leveraged to reset the offsets of consumers and consumer groups, and more. 

## Important things

#### Setting up a cluster
In order to reset offsets, there needs to be a Kafka cluster (*obviously*). Use the included Terraform in order to quickly create one using the Confluent Cloud Terraform provider. 

An important thing to note is that the Terraform template assumes you have exported the followng two environment variables to your console before running any of the Terraform commands. Those two environment variables are: `CONFLUENT_CLOUD_API_KEY` and `CONFLUENT_CLOUD_API_SECRET`. 

#### Produce some data 
Included in this example is a simple Java console application. It's intended to provide a simple set of commands you can use in order to produce data to your previously created Kafka cluster, and start, stop, and consume data from your Kafka cluster with a consumer.

To begin, open a terminal and navigate to the `consumer-offset-resetting` directory within this example. Note: the Java console application is named the same thing as this directory, so navigate to `consumer-offset-resetting/consumer-offset-resetting/`. That was intentional. 

Within the application directory, use `mvnw` to start the application. The application is an interactive shell so you can execute all the commands interactively. Use the following command to start the shell.

```bash
sh mvnw spring-boot:run
```

After Maven's yells at bunch of details to the console, you should see the console prompt `command> ` after the `consumer replay` banner. This is where you will enter commands to create data.

Start by using the produce command. By default this will create 15 messages, delayed by 1 second each (to demonstrat seeking offsets by timestamp later on).
```bash
command> produce
```

Once the 15 messages have been produced, the application should wait for another command. 

#### Consume some data
Before consuming data, you'll need to start the consumer (we have start/stop commands for the consumer because you need to stop consumers to reset offsets).
```bash
command> consumer start
```

This should return almost immediately. Next, use the `consume` command to consume the 15 records produced. Note, if the command returns without outputting any records, just run it again. This application isn't perfect. 
```bash
command> consumer consume
```

Once you see the records in the console, stop the consumer.
```bash
command> consumer stop
```

Keep the interactive shell open, but continue. 
 
#### Resetting offsets
Open another terminal, make sure you're within the application directory like above, and use the following command to create a container where you can run your `kafka-consumer-groups` commands. 
```bash
docker run -it --rm -v $(pwd)/client.properties:/home/appuser/client.properties:ro -exec confluentinc/cp-server /bin/sh
```

You should get a shell inside the container. This container is an instance of Confluent Platform Server and has all the bundled Kafka tooling within `/bin/`.

To see the options for `kafka-consumer-groups`, perform a simple root command.
```bash
sh /bin/kafka-consumer-groups
```

Next, describe your consumer group to see the committed offsets per partition. Note, you need to add the `bootstrap-server` manually. It's within `client.properties` which you can find within the application directory. 
```bash
sh /bin/kafka-consumer-groups --bootstrap-server <replace-with-your-bootstrap-server> --command-config client.properties --group replay-consumer --describe
```

You should see that the current offset and log-end offsets per partition are the same (assuming you succeeded with the `command> consumer consume` step). To reset the offsets to the earliest offset position, use the following command.
```bash
sh /bin/kafka-consumer-groups --bootstrap-server <replace-with-your-bootstrap-server> --command-config client.properties --group replay-consumer --topic replay-topic --reset-offsets --to-earliest --execute
```

You should see the new offsets per partition set to a new value (likely 0). If you received an error stating that the consumer is not inactive, stop the consumer again and/or give Kafka a minute to reconcile the consumers leaving the group. Re-run command to test it, or re-use the describe command above with the `--state` flag to print the state of the group to the console ("Empty" is the desired state to be able to reset the offsets for the group).

Now, try consuming data again by starting the consumer, and then using the consume command like the following (as before, retry the consume command if it doesn't return anything).
```bash
command> consumer start
command> consumer consume
```

One of the consume attempts should output the previously consumed records. To verify, you can conpare the timestamps and UUID's from the previous consume operation.

Next, stop the consumer with `command> consumer stop`, then use your other terminal to add the next offset resetting command. This time, you'll reset the offsets to a particular offset by number.
```bash
sh /bin/kafka-consumer-groups --bootstrap-server <replace-with-your-bootstrap-server> --command-config client.properties --group replay-consumer --topic replay-topic --reset-offsets --to-offset 1 --execute
```

Instead of resetting to earliest, you reset the offset to 1 per partition. You should see the new offsets are now equal to 1 (of the closest thing to it <1). As before, now start your consumer again and consume the records (you know the drill with the commands).

This time, you shouldn't see all the same messages. Some of them won't be printed to the console because we didn't reset the offsets to before them. Stop the consumer again.

Finally, you'll reset the offsets to a specific timestamp (this is why we print them). Copy one of the timestamps from any of the previous outputs (but not an record timestamp that is latest and won't be very interesting, ieally choose a record number that is a low number). 
```bash
sh /bin/kafka-consumer-groups --bootstrap-server <replace-with-your-bootstrap-server> --command-config client.properties --group replay-consumer --topic replay-topic --reset-offsets --to-datetime <timestamp> --execute
```

The `kafka-consumer-groups` tool will reset the offsets to the offset per partition that is the greatest value less than the given timestamp. The results you'll see for the new offset values will value depending on which timestamp you selected.

Finally, start the consumer again and consume the messages. You shouldn't see any messages older than the timestamp you selected.

Once you see results in the console, you're all done. You can exit the interactive shell using the `command> exit` command and tear down your cluster with Terraform. 

#### Conclusion
You should now have a grasp on how you can use a tool like `kafka-consumer-groups` in order to reset, describe, and managed consumer offsets. `kafka-consumer-groups` is not the only way you can reset offsets, but the the principles you used should be consistent to other methods of managing offsets (like using the Kafka AdminCient).
