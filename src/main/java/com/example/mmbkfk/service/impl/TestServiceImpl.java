package com.example.mmbkfk.service.impl;

import com.example.mmbkfk.entity.User;
import com.example.mmbkfk.mapper.TestMapper;
import com.example.mmbkfk.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    TestMapper testMapper;

    @Override
    public User findById(int id) {
        return testMapper.findById(id);
    }

    @Override
    public int insertOrUpdateUser(User user){
        return testMapper.insertOrUpdateUser(user);
    }

}
