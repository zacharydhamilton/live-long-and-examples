# Create a Custom Instance of Postgres

Whether your actually testing something or trying to create a demo, having a good "real fake instance" is useful. Follow the instructions below in order to create a custom instance of Postgres that can be used with a Postgres CDC Connector (or something similar) and Confluent Cloud. 

## Important things

#### Seeding data
The `data/` directory is meant for **CSV** files that contain seed data for the tables you want to create. An example CSV is included in that directory as a reference for understanding the formatting, etc. A good tool to create this data is something like [Mockaroo](https://www.mockaroo.com/). 

The Dockerfile that builds everything into a custom image will copy everything from this data directory auto-magically. It's not aware of file type or anything like that, so if it's different than CSV, that's fine but isn't something that this repo provides an example for. 

#### Database server config
Within the `scripts/` directory, there is a file `example.sh`. If you want to configure anything to the database or database server that requires a restart in order to take affect--this is where you do it. In `example.sh`, there is an example of doing this with the addition of `pg_cron` (a tool for scheduling procedures, statements, etc). Follow this general example if you need to make similar config changes to the `postgres.conf` file of the database. 

The Dockerfile previously mentioned will copy anything in the `scripts/` directory, however, the entrypoint script for the instance will execute `*.sh` first before anything `*.sql`, which will be discussed next.

#### Database config
Also within the `scripts/` directory, there is a file `example.sql`. If you want to create anything within the database on startup (tables, procedures, etc)--this is where you do it. In `example.sql`, there is an example of creating a table, then seeding that table with data from the `data/` directory. 

The entrypoint of the included Dockerfile will execute anything `*.sql` after anything `*.sh`, as previously mentioned. So anything you put in the `scripts/` directory will be executed that way. However, it's not clear what the ordering of execution is for multiple `.sh` or `.sql` files in the directory. 

#### Conclusion
Using the above, you should be able to make a half-decent instance of Postgres that you can use to generate "real fake data". 