# Streaming Rule Engine

The above repository contains the source and test files required to run a rule engine service.

The details of the design is given in the PDF included in the folder.


Maven has been used to build.
mvn package generates the required jar file :target/rule_engine-1.0-SNAPSHOT.jar

To run the jar : it take kafka broker and kafka topic as input
java -cp target/rule_engine-1.0-SNAPSHOT.jar com.rule_engine.StreamRuleEngine 0.0.0.0:9092 stream_data_sample_2

Needs:
Kafka and Mongo DB services to be running.




