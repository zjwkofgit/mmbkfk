package com.example.mmbkfk.service;

import com.example.mmbkfk.entity.User;

public interface TestService {
    User findById(int id);
    int insertOrUpdateUser(User user);
}
