package com.example.mmbkfk.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    private static final long serialVersionUID = -1242493306307174690L;
    private Integer id;
    private String name;
    private Integer age;
}
