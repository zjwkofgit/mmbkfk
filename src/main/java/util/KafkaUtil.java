//package util;
//
//import com.alibaba.fastjson2.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.admin.*;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.clients.consumer.OffsetAndMetadata;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.Producer;
//import org.apache.kafka.common.Node;
//import org.apache.kafka.common.PartitionInfo;
//import org.apache.kafka.common.TopicPartition;
//
//import java.util.*;
//import java.util.concurrent.ExecutionException;
//import java.util.stream.Collectors;
//
///**
// * @program: kafkaUI
// * @description:
// * @author: jiangqiang
// * @create: 2020-10-28 20:05
// **/
//@Slf4j
//public class KafkaUtil {
//
//    public static AdminClient createAdminClientByProperties(String brokers) {
//
//        Properties prop = new Properties();
//
//        prop.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
//        prop.setProperty(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "2000");
//        prop.setProperty(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, "2000");
//
//        return AdminClient.create(prop);
//    }
//
//
//    public static void createTopic(String brokers, String topic, Integer partition, Integer replica) throws Exception {
//        AdminClient adminClient = null;
//        try {
//            adminClient = createAdminClientByProperties(brokers);
//            List<NewTopic> topicList = new ArrayList();
//            NewTopic newTopic = new NewTopic(topic, partition, replica.shortValue());
//            topicList.add(newTopic);
//            CreateTopicsResult result = adminClient.createTopics(topicList);
//            result.all().get();
//            result.values().forEach((name, future) -> System.out.println("topicName:" + name));
//        } catch (Exception e) {
//
//        } finally {
//
//            adminClient.close();
//        }
//
//    }
//
//    public static Producer<String, String> getProducer(String brokers) {
//
//        Properties props = new Properties();
//        props.put("bootstrap.servers", brokers);
//        props.put("acks", "all");
//        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//
//        Producer<String, String> producer = new KafkaProducer<>(props);
//
//        return producer;
//
//    }
//
//    public static KafkaConsumer<String, String> getConsumer(String brokers, String topic, String group, String offset) {
//        Properties props = new Properties();
//        props.setProperty("bootstrap.servers", brokers);
//        props.setProperty("group.id", group);
//        props.setProperty("enable.auto.commit", "true");
//        props.setProperty("auto.commit.interval.ms", "1000");
//        props.setProperty("auto.offset.reset", offset);
//        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
//
//        consumer.subscribe(Collections.singleton(topic));
//        return consumer;
//
//    }
//
//    public static KafkaConsumer<String, String> getConsumer(String brokers, Collection<String> topics, String group, String offset) {
//        Properties props = new Properties();
//        props.setProperty("bootstrap.servers", brokers);
//        props.setProperty("group.id", group);
//        props.setProperty("enable.auto.commit", "true");
//        props.setProperty("auto.commit.interval.ms", "1000");
//        props.setProperty("auto.offset.reset", offset);
//        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
//
//        consumer.subscribe(topics);
//        return consumer;
//
//    }
//
//    public static void main(String[] args) throws Exception {
//
//        KafkaConsumer<String, String> consumer = getConsumer("120.48.0.111:9092", "zjwhaha", "test", "earliest");
//        List<PartitionInfo> partitionInfoSet = consumer.partitionsFor("zjwhaha");
//
//        List<TopicPartition> collect = partitionInfoSet.stream().map(partitionInfo -> new TopicPartition(partitionInfo.topic(), partitionInfo.partition()))
//                .collect(Collectors.toList());
////        consumer.assign(collect);
//        Map<TopicPartition, Long> topicPartitionLongMap = consumer.endOffsets(collect);
//        Map<TopicPartition, Long> topicPartitionLongMap1 = consumer.beginningOffsets(collect);
//
////        consumer.poll(Duration.ofMillis(0));
////        consumer.assign(collect);
////        List<Long> positions = collect.stream().map(t -> {
////            long position = consumer.position(t);
////            return position;
////        }).collect(Collectors.toList());
//
//        System.out.println();
//    }
//
//    public static void deleteTopic(String broker, String name) {
//        AdminClient adminClient = createAdminClientByProperties(broker);
//        List<String> list = new ArrayList<>();
//        list.add(name);
//        adminClient.deleteTopics(list);
//        adminClient.close();
//    }
//
//    public static JSONObject node2Json(Node node) {
//        JSONObject leaderNode = new JSONObject();
//        leaderNode.put("id", node.id());
//        leaderNode.put("host", node.host());
//        leaderNode.put("port", node.port());
//        leaderNode.put("rack", node.rack());
////        leaderNode.put("port",node.port());
//        return leaderNode;
//    }
//
//    public static JSONObject getTopicDetail(String broker, String topic) throws Exception {
//        AdminClient adminClient = createAdminClientByProperties(broker);
//
//        List<String> list = new ArrayList<>();
//        list.add(topic);
//        DescribeTopicsResult result = adminClient.describeTopics(list);
//        Map<String, TopicDescription> map = result.all().get();
//        TopicDescription topicDescription = map.get(topic);
//
//        JSONObject res = new JSONObject();
//        res.put("isInternal", topicDescription.isInternal());
//        res.put("name", topicDescription.name());
//
//        KafkaConsumer<String, String> consumer = getConsumer(broker, topic, "KafkaUI-lite", "earliest");
//        List<TopicPartition> topicPartitions = topicDescription.partitions().stream().map(t -> {
//            TopicPartition topicPartition = new TopicPartition(topic, t.partition());
//            return topicPartition;
//        }).collect(Collectors.toList());
//        Map<TopicPartition, Long> endOffsets = consumer.endOffsets(topicPartitions);
//        Map<TopicPartition, Long> beginningOffsets = consumer.beginningOffsets(topicPartitions);
//
//        List<JSONObject> collect = topicDescription.partitions().stream().map(t -> {
//            JSONObject p = new JSONObject();
//            Node leader = t.leader();
//            log.info(leader.toString());
//
//            List<JSONObject> replicas = t.replicas().stream().map(r -> node2Json(r)).collect(Collectors.toList());
//
//            List<JSONObject> isr = t.isr().stream().map(r -> node2Json(r)).collect(Collectors.toList());
//            Long endOffset = endOffsets.get(new TopicPartition(topic, t.partition()));
//            Long beginningOffset = beginningOffsets.get(new TopicPartition(topic, t.partition()));
//
//            p.put("partition", t.partition());
//
////            leaderNode.put("id",leader.id());
//            p.put("leader", node2Json(leader));
//            p.put("replicas", replicas);
//            p.put("isr", isr);
//            p.put("endOffset", endOffset);
//            p.put("beginningOffset", beginningOffset);
//
//            return p;
//
//        }).collect(Collectors.toList());
//        res.put("partitions", collect);
//
//        System.out.println(res.toJSONString());
//        adminClient.close();
//        return res;
//    }
//
//
//
//
//
//    private static String comparingByName(JSONObject jo){
//        return jo.getString("topic");
//    }
//
//    private static Integer comparingByPartition(JSONObject jo){
//        return jo.getInteger("partition");
//    }
//
//
//}
