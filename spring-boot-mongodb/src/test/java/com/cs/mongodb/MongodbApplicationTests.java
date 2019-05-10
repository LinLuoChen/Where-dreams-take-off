package com.cs.mongodb;

import com.cs.mongodb.model.Mongo;
import com.cs.mongodb.service.impl.MongoServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongodbApplicationTests {

    @Resource
    private MongoServiceImpl mongoService;

    @Test
    public void contextLoads() {
    }

    /**
     *  MongoDB 测试
     */
    @Test
    public void MongoDBTest(){
        Mongo mongo = new Mongo();
        mongo.setId("1");
        mongo.setName("小明");
        mongoService.insertOne(mongo);
        System.out.println("成功");
        List<Mongo> list = mongoService.findAll(Mongo.class);
        for (Mongo db:list) {
            System.out.println("id为："+db.getId());
            System.out.println("姓名为："+db.getName());
        }
    }

}
