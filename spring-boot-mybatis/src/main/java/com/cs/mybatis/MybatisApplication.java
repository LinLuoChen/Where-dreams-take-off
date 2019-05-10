package com.cs.mybatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication // 声明这是个主类
@ComponentScan(basePackages = "com.cs.mybatis.*") // 扫描主程序文件
@MapperScan("com.cs.mybatis.mapper") // 扫描这个包下的 Mapper 文件
@ServletComponentScan // 使用该注解后，Servlet Filter Listener 可以直接通过
public class MybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisApplication.class, args);
    }

}
