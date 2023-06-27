# Create a custom `SubjectNameStrategy`

While it's an obscure use case, you might find yourself needing a `SubjectNameStrategy` that isn't one of the basic three `TopicNameStrategy` (default), `RecordNameStrategy`, and `TopicRecordNameStrategy`. Good news is that it's really, really easy to make your own--just be sure you understand what you're doing and how a `SubjectNameStrategy` works. 

## Important things

#### Implementing `SubjectNameStrategy`
In order to create your own `SubjectNameStrategy`, you simply need to create a class that implements it. The interface has two methods that you need to override, `configure` and `subjectName`, however, `subjectName` is what actually makes things happen (you can see in the examples here that `configure` is blank). 

It's important to understand what is actually happening in a Kafka client and what a `SubjectNameStrategy` actually is doing. In short, the `SubjectNameStrategy` you pass to a Kafka client determines how that Kafka client finds the **Subject Name**  to validate/register a schema against. The default, `TopicNameStrategy`, determines the subject name by by taking the topic name and adding `-value` or `-key` to it depending on whether it's a key or value schema. So, if you send any given event with `TopicNameStrategy` to a topic named `animals`, the corresponding subjects that the Kafka client will validate your record schema against are `animals-value` or `animals-key`. 

Ultimately, the `subjectName` method returns the string that will be used to create/search/whatever the subject for a schema. You have three pieces of data within that method, `String topic, boolean isKey, ParsedSchema schema`, in order to create whatever string your heart desires and return it to the Kafka client.

#### An example
Included within this example is a impractical `SubjectNameStrategy` named `RidiculousNameStrategy` to really demonstrate that you can do whatever you want (to some degree). Taking a look at the `subjectName` method of the class, you'll see something close to the following:
```java
// ...
private String alphabet = "abcdefghijklmnopqrstuvwxyz";
private String crazy    = "48cd3f9h1jk1mn0p9r57uvwxyz";
// ...
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
// ...
```

As you can see, this isn't practical unless you have a use case where you need some letters of the alphabet replaced by numbers in the registered subject in Schema Registry. That being said, it still works and you should see the registered subject look something like `-v41u3` or `-k3y`. 

Another example, which is still unlikely (but maybe more likely than above), you be the follow:
```java
public String subjectName(String topic, boolean isKey, ParsedSchema schema) {
    if (schema == null) {
        return null;
    } else {
        return topic.toUpperCase()+(isKey ? "-KEY" : "-VALUE");
    }
}
```

In the above case, the strategy make the subjects always uppercase. So, probably not useful, but hopefully it gets the point across. 

#### See it in action
To see this for yourself, you'll need to start by building the provided Terraform configuration. You can find it within the `terraform/` directory. This Terraform config assumes that you have environment variables `CONFLUENT_CLOUD_API_KEY` and `CONFLUENT_CLOUD_API_SECRET` in your console. Terraform uses these in order to get authorization to your account to create the resources. 

Once the configuration is set up, you can build and run the Java code with the following command:
```bash
sh mvnw spring-boot:run
```

Somewhere in everything Maven yells into the console, you should see some console prints from the Kafka Producer of the application. Once you see this, you can log into Confluent Cloud and view the messages within the topics, as well as the registered schemas and subject. If everything works as expected, you'll see the funky subject name and the all uppercase one. 

#### Conclusion
If, for whatever reason, you need to implement your own `SubjectNameStrategy`, you can and it's not that complicated.