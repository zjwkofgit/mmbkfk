//package util;
//
//import java.time.Duration;
//import java.util.Collections;
//import java.util.Properties;
//import java.util.concurrent.Future;
//
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.clients.producer.RecordMetadata;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//
///**
// * @Description kafka工具类，提供消息发送与监听
// * @url https://blog.csdn.net/qq_33460264/article/details/120198173
// * @Author chenhui
// */
//public class KafkaUtils {
//    private static final Logger log = LoggerFactory.getLogger(KafkaUtils.class);
//
//    /**
//     * 获取实始化KafkaStreamServer对象
//     *
//     * @return
//     */
//    public static KafkaStreamServer bulidServer() {
//        return new KafkaStreamServer();
//    }
//
//    /**
//     * 获取实始化KafkaStreamClient对象
//     *
//     * @return
//     */
//    public static KafkaStreamClient bulidClient() {
//        return new KafkaStreamClient();
//    }
//
//    public static class KafkaStreamServer {
//        KafkaProducer<String, String> kafkaProducer = null;
//
//        private KafkaStreamServer() {
//        }
//
//        /**
//         * 创建配置属性
//         *
//         * @param bootstrapServers kafka地址，多个地址用逗号分割
//         * @return
//         */
//        public KafkaStreamServer createKafkaStreamServer(String bootstrapServers) {
//            if (kafkaProducer != null) {
//                return this;
//            }
//            Properties properties = new Properties();
//            // kafka地址，多个地址用逗号分割
//            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//
//            // 用户名密码方式 begin
//            properties.put("sasl.jaas.config",
//                    "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"userName\" password=\"passWord\";");
//
//            properties.put("security.protocol", "SASL_PLAINTEXT");
//            properties.put("sasl.mechanism", "SCRAM-SHA-256");
//            // 用户名密码方式 end
//            kafkaProducer = new KafkaProducer<>(properties);
//            return this;
//        }
//
//        /**
//         * 向kafka服务发送生产者消息
//         *
//         * @param topic
//         * @param msg
//         * @return
//         */
//        public Future<RecordMetadata> sendMsg(String topic, String msg) {
//            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, msg);
//            Future<RecordMetadata> future = kafkaProducer.send(record);
//            try {
//                log.info("topic:[" + topic + "]消息发送成功:" + future.get().toString());
//            } catch (Exception e) {
//                e.printStackTrace();
//                log.info("topic:[" + topic + "]消息发送失败：" + msg);
//            }
//            return future;
//        }
//
//        /**
//         * 关闭kafka连接
//         */
//        public void close() {
//            if (kafkaProducer != null) {
//                kafkaProducer.flush();
//                kafkaProducer.close();
//                kafkaProducer = null;
//            }
//        }
//    }
//
//    public static class KafkaStreamClient {
//        KafkaConsumer<String, String> kafkaConsumer = null;
//
//        private KafkaStreamClient() {
//        }
//
//        /**
//         * 配置属性,创建消费者
//         *
//         * @param host
//         * @param port
//         * @return
//         */
//        public KafkaStreamClient createKafkaStreamClient(String host, int port, String groupId) {
//            String bootstrapServers = String.format("%s:%d", host, port);
//            if (kafkaConsumer != null) {
//                return this;
//            }
//            Properties properties = new Properties();
//            // kafka地址，多个地址用逗号分割
//            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//            properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//            properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//            properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
//            kafkaConsumer = new KafkaConsumer<String, String>(properties);
//            return this;
//        }
//
//        /**
//         * 客户端消费者拉取消息，并通过回调HeaderInterface实现类传递消息
//         *
//         * @param topic
//         * @param headerInterface
//         */
//        public void pollMsg(String topic, HeaderInterface headerInterface) {
//            kafkaConsumer.subscribe(Collections.singletonList(topic));
//            while (true) {
//                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
//                for (ConsumerRecord<String, String> record : records) {
//                    try {
//                        headerInterface.execute(record);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//
//        /**
//         * 关闭kafka连接
//         */
//        public void close() {
//            if (kafkaConsumer != null) {
//                kafkaConsumer.close();
//                kafkaConsumer = null;
//            }
//        }
//    }
//
//    @FunctionalInterface
//    interface HeaderInterface {
//        void execute(ConsumerRecord<String, String> record);
//    }
//
//    /**
//     * 测试示例
//     *
//     * @param args
//     * @throws InterruptedException
//     */
//    public static void main(String[] args) throws InterruptedException {
//        // 生产者发送消息
//        KafkaStreamServer kafkaStreamServer = KafkaUtils.bulidServer().createKafkaStreamServer("120.48.0.111:9092");
//        int i = 0;
//        while (i < 10) {
//            String msg = "this is a message for kafka:" + i;
//            kafkaStreamServer.sendMsg("zjwhaha", msg);
//            i++;
//            Thread.sleep(100);
//        }
//        kafkaStreamServer.close();
//        System.out.println("发送结束");
//
////		System.out.println("接收消息");
////		KafkaStreamClient kafkaStreamClient = KafkaUtils.bulidClient().createKafkaStreamClient("127.0.0.1", 9092,
////				"consumer-45");
////		kafkaStreamClient.pollMsg("test", new HeaderInterface() {
////			@Override
////			public void execute(ConsumerRecord<String, String> record) {
////				System.out.println(
////						String.format("topic:%s,offset:%d,消息:%s", record.topic(), record.offset(), record.value()));
////			}
////		});
//
//    }
//
//}
