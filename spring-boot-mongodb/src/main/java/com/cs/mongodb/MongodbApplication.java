package com.cs.mongodb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.cs.mongodb.*") // 扫描主程序文件
@MapperScan("com.cs.mongodb.mapper") // 扫描这个包下的 Mapper 文件
@ServletComponentScan // 使用该注解后，Servlet Filter Listener 可以直接通过
public class MongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongodbApplication.class, args);
    }

}
