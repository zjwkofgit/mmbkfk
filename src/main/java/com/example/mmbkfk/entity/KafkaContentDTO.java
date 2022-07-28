package com.example.mmbkfk.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class KafkaContentDTO implements Serializable {
    private static final long serialVersionUID = -1242493306307174690L;
    private String topic;
    private String msg;
}
