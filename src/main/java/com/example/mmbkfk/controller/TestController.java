package com.example.mmbkfk.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.mmbkfk.config.WebSocketServer;
import com.example.mmbkfk.entity.KafkaContentDTO;
import com.example.mmbkfk.entity.User;
import com.example.mmbkfk.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
public class TestController {
    private final static String TOPIC_NAME = "zjwqq"; //topic的名称

    @Autowired
    TestService testService;

    @Resource
    KafkaTemplate kafkaTemplate;

    @Autowired
    WebSocketServer webSocketServer;

    @RequestMapping("/hi")
    public String hi(){
        log.info("hi");
//        kafkaTemplate.send(TOPIC_NAME, "1111");
        return "hi-zjw";
    }

    @RequestMapping("/sw")
    public String sendWebsocketMsg(){
        log.info("sendWebsocketMsg");
        JSONObject obj = new JSONObject();
        obj.put("cmd", "topic");//业务类型
        obj.put("msgId", 11223);//消息id
        obj.put("msgTxt", "一一二二三");//消息内容
        webSocketServer.sendAllMessage(obj.toJSONString());
        return "OK,sendWebsocketMsg";
    }

    @RequestMapping("/user/{id}")
    public User getUser(@PathVariable int id){
        log.info("getUser123");
        testService.findById(id);
        return testService.findById(id);
    }

    @RequestMapping("/user/add")
    public User addUser(@RequestBody User user){
        log.info("addUser");
        testService.insertOrUpdateUser(user);
        return user;
    }

    @PostMapping("/send")
    public String sendMsg(@RequestBody KafkaContentDTO kafkaContent){
        log.info("sendMsg");
        kafkaTemplate.send(kafkaContent.getTopic(), kafkaContent.getMsg());
        return "ok";
    }


    /**
     * 消费者配置，kafka.topic为监听的topic，kafka.group为消费分组，可在yml中修改
     */
    @KafkaListener(topics = TOPIC_NAME, groupId = "zjwGroup1")
    @Async
    public void kafkaListener(ConsumerRecord<String, String> consumerRecord) {
        String value = consumerRecord.value();
        if (log.isInfoEnabled()) {
            log.info("读取到消息：offset {}, value {}", consumerRecord.offset(), value);
            webSocketServer.sendAllMessage(value);
        }
        if (null == value) {
            log.error("kafka消费数据为空");
        }
    }




}
