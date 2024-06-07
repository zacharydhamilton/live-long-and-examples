# Referencing a JSON schema in another schema with Schema Registry

If you have schemas that are types that would ultimately get referenced in other schemas, for instance, like a custom/specific datetime format--you'll want to use reference schemas. They're a convenient way to inject abstract objects and types into your other schemas while still having versioning and other things. In this repo, there is an example of doing things with a simple Peanut butter and Jelly sandwhich schema. 

## Important things

#### Terraform

The first part of setting up this example is building everything with Terraform. This should set up all the correct infra required in order to run the included Java application and produce a message to Conluent CLoud.

#### Java application

The second part of this example is the Java application that produces "sandwhiches" using the PBJ schema. It creates a sandwhich by creating Peanutbutter and Jelly objects and adding them to a PBJ object as fields. See the following.

```java
PeanutButter pb = new PeanutButter();
pb.setSmooth(true);
pb.setOrganic(true);
Jelly j = new Jelly();
j.setFlavor("grape");
j.setOrganic(true);
PBJ pbj = new PBJ();
pbj.setBread("wheat");
pbj.setPeanutbutter(pb);
pbj.setJelly(j);
```

These classes will be created from the `PBJ.json` schema. While the other two schema files exist in the same directory, we're going to skip them. We keep them in the directory so they're checked into source control and can be referenced with using the URL to their raw content. The following plugin will fetch the schemas from source control and use them to both build the POJOs for use in the Java code, but it will also make sure that their definition is included within the schema that gets registered with Schema Registry when you produce the event. 

```xml
<plugin>
    <groupId>org.jsonschema2pojo</groupId>
    <artifactId>jsonschema2pojo-maven-plugin</artifactId>
    <version>${jsonschema2pojo-maven-plugin}</version>
    <configuration>
        <sourceDirectory>${basedir}/../schema-files/</sourceDirectory>
        <targetPackage>com.github.zacharydhamilton.objects</targetPackage>
        <outputDirectory>src/main/java/</outputDirectory>
        <excludes>
        <exclude>PeanutButter.json</exclude>
        <exclude>Jelly.json</exclude>
        </excludes>
    </configuration>
    <executions>
        <execution>
        <goals>
            <goal>generate</goal>
        </goals>
        </execution>
    </executions>
</plugin>
```

#### Produce a record

Use whatever your favorite way to run a Java application is, which should send a single PBJ sandwhich event to the Kafka topic. 

#### Conclusion

If you find yourself with frequently reused "types" in many schemas, consider registering it under its own schema and referencing it where needed. You'll be able to version it independently in referenced locations, but ultimately manage one single copy of it. 