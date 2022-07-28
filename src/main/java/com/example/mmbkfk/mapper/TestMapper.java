package com.example.mmbkfk.mapper;

import com.example.mmbkfk.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TestMapper  {

    @Select("select * from t_user where id = #{id}")
    User findById(int id);

    @Insert("insert into t_user (id,name,age) value(#{user.id},#{user.name},#{user.age}) on duplicate key update name = #{user.name}")
    int insertOrUpdateUser(@Param("user") User user);


}
