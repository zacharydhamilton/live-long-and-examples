### Download CP
curl -O https://packages.confluent.io/archive/7.5/confluent-7.5.2.tar.gz

### Unzip CP 
tar xzf confluent-7.5.2.tar.gz

### Run tests
sh bin/kafka-producer-perf-test --num-records 60000 --record-size 1000 --throughput 1000 --topic six-partitions --producer.config client.properties --producer-props acks=1 linger.ms=5
sh bin/kafka-producer-perf-test --num-records 600000 --record-size 100 --throughput 10000 --topic six-partitions --producer.config client.properties --producer-props acks=-1 linger.ms=0

sh bin/kafka-consumer-perf-test --bootstrap-server <bootstrap_server> --consumer.config client.properties --topic six-partitions --fetch-size 1048576 --messages 60000