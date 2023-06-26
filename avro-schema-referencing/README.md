# Referencing an Avro schema in another with Schema Registry

If you have schemas that are types that would ultimately get referenced in other schemas, for instance, like a custom/specific datetime format--you'll want to use reference schemas. They're a convenient way to inject abstract objects and types into your other schemas while still having versioning and other things. In this repo, there is an example of doing things with a simple Peanut butter and Jelly sandwhich schema. 

## Important things

#### Terraform

The first part of setting up this example is building everything with Terraform. The most important and interesting thing to examine are the Schema Registry specific resources (everything else is pretty standard and not specific to this example). These Schema Registry resources will handle the registration of you schemas and subjects build directly from schema files. 

Take a look at the directory `schema-files/` and you'll see three schemas: `PBJ.avsc`, `Peanutbutter.avsc`, and `Jelly.avsc`. If you take a closer look at `PBJ.avsc` you will see that there are two fields defined which reference the other two schemas. In order to make this work when registering the schemas with Schema Registry, you simply need to register these schemas and then provide a reference to them when registering the third. For example, take a look at the resource for the `PBJ` schema. 

```hcl
resource "confluent_schema" "pbj" {
    subject_name = "sandwhiches-value"
    format = "AVRO"
    schema = file("../schema-files/PBJ.avsc")
    rest_endpoint = confluent_schema_registry_cluster.main.rest_endpoint
    schema_reference {
        name = "com.github.zacharydhamilton.events.PeanutButter"
        subject_name = "com.github.zacharydhamilton.events.PeanutButter"
        version = confluent_schema.peanutbutter.version
    }
    schema_reference {
        name = "com.github.zacharydhamilton.events.Jelly"
        subject_name = "com.github.zacharydhamilton.events.Jelly"
        version = confluent_schema.jelly.version
    }
    credentials {
        key = confluent_api_key.app_manager_sr.id
        secret = confluent_api_key.app_manager_sr.secret
    }
    schema_registry_cluster {
        id = confluent_schema_registry_cluster.main.id
    }
}
```

To see more, just explore the `confluent_schema` resources in the Terraform. 

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

These classes we all created from the schema files in the `schema-files/` directory (so both Terraform and the application use the same artifact which can be source controlled and versioned to build instances of objects in the codebase and in the Schema Registry). The Java application uses a Maven plugin to create the Avro classes from the schema files automatically as a step in the build/compile processes. See the following from `pom.xml` as reference. 

```xml
<plugin>
    <groupId>org.apache.avro</groupId>
    <artifactId>avro-maven-plugin</artifactId>
    <version>1.11.0</version>
    <executions>
    <execution>
        <phase>generate-sources</phase>
        <goals>
        <goal>schema</goal>
        </goals>
        <configuration>
        <sourceDirectory>${project.basedir}/../schema-files</sourceDirectory>
        <imports>
            <import>${project.basedir}/../schema-files/PeanutButter.avsc</import>
            <import>${project.basedir}/../schema-files/Jelly.avsc</import>
        </imports>
        <includes>
            <include>PBJ.avsc</include>
        </includes>
        <outputDirectory>${project.basedir}/src/main/java/</outputDirectory>
        </configuration>
    </execution>
    </executions>
</plugin>
```

The classes created by this plugin are determined by what's definined in the schema files. The path is determined by the namespace and the classname is determined by the name. From there, everything else is defined by the fields of the schema. When it's done, you get classes with all the getter's and setter's to use (like above). 

#### Produce a record

In order to produce a record to Kafka and actually see how things look, run he application. It will produce a single message to a topic created by Terraform. 

```bash
sh mvnw spring-boot:run
```

Maven will yell a bunch on information into the console as it builds/compiles the application, but should console print a message indicating the record produced to Kafka towards the end. It should be prepended with `--->` and then some information about what it produced. From there, you can go to your Kafka cluster and view the events in the topic. 

#### Conclusion

If you find yourself with frequently reused "types" in many schemas, consider registering it under its own schema and referencing it where needed. You'll be able to version it independantly in referenced locations, but ultimately manage one single copy of it. 