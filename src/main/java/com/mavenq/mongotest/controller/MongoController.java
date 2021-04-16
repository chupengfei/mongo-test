package com.mavenq.mongotest.controller;

import com.alibaba.fastjson.JSONObject;
import com.mavenq.mongotest.entity.User;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/demo")
public class MongoController {
    /**
     * 注入 MongoDB 模板类，由 Spring 进行创建和维护
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 获取数据库名称和类名
     * @return
     */
    @RequestMapping("/info")
    public String info() {
        MongoDatabase mongoDatabase = mongoTemplate.getDb();
        JSONObject obj = new JSONObject();
        obj.put("dbName", mongoDatabase.getName());
        obj.put("class", mongoDatabase.getClass().toString());
        return obj.toJSONString();
    }


    /**
     * 获取用户列表
     * @return
     */
    @RequestMapping("/list")
    public String list() {
        List<User> users = mongoTemplate.findAll(User.class, "users");
        return JSONObject.toJSONString(users);
    }


    /**
     * 添加用户信息
     * @param id 用户ID
     * @param name 用户名
     * @param age 年龄
     * @return
     */
    @RequestMapping("/add")
    public String add(@RequestParam(value = "id", defaultValue = "-1") int id,
                      @RequestParam(value = "name", defaultValue = "test") String name,
                      @RequestParam(value = "age", defaultValue = "20") Integer age) {
        if(id <= 0) {
            return "用户ID不能小于0";
        }

        // 根据用户ID判断用户是否存在
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        List<User> userList = mongoTemplate.find(query, User.class);
        if(userList != null && userList.size() > 0) {
            return "ID=" + id + "的用户已经存在，不能添加用户";
        }

        // 添加用户
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setAge(age);
        mongoTemplate.save(user, "users");
        return JSONObject.toJSONString(user);
    }


    /**
     * 根据ID更新文档
     * @param id 用户ID
     * @param name 用户名
     * @param age 年龄
     * @return
     */
    @RequestMapping("/update")
    public String update(@RequestParam(value = "id", defaultValue = "-1") int id,
                         @RequestParam(value = "name", defaultValue = "test") String name,
                         @RequestParam(value = "age", defaultValue = "20") Integer age) {
        // 根据用户ID判断用户是否存在
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        List<User> userList = mongoTemplate.find(query, User.class);
        if(userList == null || userList.isEmpty()) {
            return "ID=" + id + "的用户不存在，不能修改用户";
        }

        // 更新数据
        Update update = new Update();
        update.addToSet("name", name);
        update.addToSet("age", age);
        UpdateResult result = mongoTemplate.updateMulti(query, update, User.class);
        return JSONObject.toJSONString(result);
    }


    /**
     * 根据ID删除用户
     * @param id 用户ID
     * @return
     */
    @RequestMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        // 根据用户ID字段删除MongoDB文档
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));

        // 删除用户信息
        DeleteResult deleteResult = mongoTemplate.remove(query, "users");
        return JSONObject.toJSONString(deleteResult);
    }

}
