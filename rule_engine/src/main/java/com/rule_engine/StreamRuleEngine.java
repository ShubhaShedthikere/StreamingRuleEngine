package com.rule_engine;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.FilterFunction;
import scala.Tuple2;
import com.mongodb.spark.MongoSpark;
import org.bson.Document;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import java.util.HashSet;
import java.util.Set;
import kafka.serializer.StringDecoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;

import scala.collection.JavaConverters;
import scala.collection.Seq;

public final class StreamRuleEngine {


    private StreamRuleEngine() {
    }

    private static Boolean isValidData(Row row, List<String> requiredFieldNames){

        // extract the required fields from the Row
        Seq scala_seq_required_names = JavaConverters.asScalaBufferConverter(requiredFieldNames).asScala().toSeq();
        Map<String,String> requiredFieldsMap = (Map<String,String>) JavaConverters.mapAsJavaMapConverter(row.getValuesMap(scala_seq_required_names)).asJava();

        // check the rule using the rule matcher method
        return RuleMatcher.rule(requiredFieldsMap);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: StreamRuleEngine <brokers> <topics>\n" +
                    "  <brokers> is a list of one or more Kafka brokers\n" +
                    "  <topics> is a list of one or more kafka topics to consume from\n\n");
            System.exit(1);
        }

        //StreamingExamples.setStreamingLogLevels();

        String brokers = args[0];
        String topics = args[1];

        SparkConf sparkConf = new SparkConf().setAppName("StreamRuleEngine").setMaster("local[*]")
                .set("spark.mongodb.input.uri", "mongodb://127.0.0.1:27017/ruleEngine.rules_set_3");

        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        // Create context with a 2 seconds batch interval for Streaming
        JavaStreamingContext jssc = new JavaStreamingContext(jsc, Durations.seconds(5));

        Set<String> topicsSet = new HashSet<>(Arrays.asList(topics.split(",")));
        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("metadata.broker.list", brokers);

        // Create direct kafka stream with brokers and topics
        JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(
                jssc,
                String.class,
                String.class,
                StringDecoder.class,
                StringDecoder.class,
                kafkaParams,
                topicsSet
        );

        // Get the value from the kafka message
        JavaDStream<String> lines = messages.map(Tuple2::_2);

        JavaDStream<EventMessage> eventsData = lines.map(new Function <String, EventMessage>() {
                                                        public EventMessage call(String x) throws Exception {
                                                            ObjectMapper objmapper = new ObjectMapper();
                                                            return objmapper.readValue(x, EventMessage.class);
                                                        }}
                                                        );

        // For each RDD, join with the rules DataFrame, filter out the rows of the DataFrame and give out the filtered Data
        eventsData.foreachRDD((rdd,time)->  {
            // Get the singleton instance of SparkSession
            System.out.println(rdd.take(5));

            SparkSession spark = SparkSession.builder().config(rdd.context().getConf()).getOrCreate();

            // Create DataFrame of EventsData
            Dataset<Row> eventsDataFrame = spark.createDataFrame(rdd,EventMessage.class);

            // Creates a temporary view using the DataFr
            eventsDataFrame.createOrReplaceTempView("words");
            eventsDataFrame.show();

            // Create DataFrame of rules Data from Mongo
            JavaRDD<Document> rules_rdd = MongoSpark.load(jsc);
            System.out.println(rules_rdd.take(5));

            JavaRDD<RuleFormat> rules_rdd_formatted = rules_rdd.map(new Function<Document, RuleFormat>() {
                                                                        @Override
                                                                        public RuleFormat call(Document s) throws Exception {
                                                                            RuleFormat out_rule = new RuleFormat();
                                                                                out_rule.setRuleSignal((String)s.get("signal"));
                                                                                out_rule.setRuleType((String)s.get("value_type"));
                                                                                out_rule.setRuleValue((String)s.get("rule_value"));
                                                                                out_rule.setRuleOperation((String)s.get("rule_operation"));
                                                                                return out_rule;
                                                                            }
                                                                        });


            Dataset<Row> ruleDataFrame = spark.createDataFrame(rules_rdd_formatted,RuleFormat.class);
            //ruleDataFrame.show();

            // join the rules and events data
            Dataset<Row> joined_df = eventsDataFrame.join(ruleDataFrame
                                                         ,eventsDataFrame.col("signal")
                                                                       .equalTo(ruleDataFrame.col("ruleSignal"))
                                                               .and(eventsDataFrame.col("valueType")
                                                                       .equalTo(ruleDataFrame.col("ruleType")))
                                                         , "inner");
            //joined_df.show();


            // filter out those rows which do not satisfy the condition
            ArrayList<String> requiredFields = RuleMatcher.getRequiredFieldNames();
            FilterFunction<Row> dataFilter = e -> isValidData(e,requiredFields);
            Dataset<Row> filtered_df = joined_df.filter(dataFilter);
            filtered_df.show();
            });


        // Start the computation
        jssc.start();
        jssc.awaitTermination();
    }


}