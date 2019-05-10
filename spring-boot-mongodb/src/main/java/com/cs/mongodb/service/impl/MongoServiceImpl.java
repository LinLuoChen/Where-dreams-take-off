package com.cs.mongodb.service.impl;

import com.cs.mongodb.model.Mongo;
import com.cs.mongodb.repository.BaseMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @InterfaceName MongoService
 * @Param
 * @Author linluochen
 * @Date 2019/5/3 10:40
 * @Version 1.0
 **/
@Service
public class MongoServiceImpl extends BaseMongoService<Mongo> {  // 实现类继承的是 MongoDBService 一定要注意

    @Autowired
    private MongoTemplate mongoTemplate;

    public Map<String,String> insertOne(Mongo Mongo) {
        Map<String,String> relMap = new HashMap<String,String>();
        try {
            mongoTemplate.insert(Mongo);
            relMap.put("relMsg","success");
        } catch (Exception e) {
            e.printStackTrace();
            relMap.put("relMsg","error");
        }
        return relMap;
    }

}
