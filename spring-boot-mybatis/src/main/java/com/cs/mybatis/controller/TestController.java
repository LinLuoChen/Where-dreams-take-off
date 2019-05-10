package com.cs.mybatis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName TestController
 * @Author linluochen
 * @Date 2019/4/19 16:09
 * @Version 1.0
 **/
@Controller // 声明这是一个 Controller 表示此类用于负责处理 Web 请求
public class TestController {

    @RequestMapping("Test") // 如果参数链接的请求的参数相同则调用此方法
    @ResponseBody // 表示这个的返回值只一个文本不是一个视图路径  通常用于接收 AJAX 请求
    public String Test(){
        System.out.println("来了老弟？");
        return "Hello World";
    }

}
