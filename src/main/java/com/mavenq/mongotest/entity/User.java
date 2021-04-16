package com.mavenq.mongotest.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// @Document 注释此类将被映射到数据库的一个集合（collection为集合名称）
@Document(collection = "users")
public class User {
    @Field("id")
    private long id;

    @Field("name")
    private String name;

    @Field("age")
    private int age;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
