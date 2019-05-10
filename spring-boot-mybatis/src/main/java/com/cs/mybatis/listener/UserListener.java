package com.cs.mybatis.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @ClassName UserListener
 * @Param 监听器
 * @Author linluochen
 * @Date 2019/4/23 13:58
 * @Version 1.0
 **/
@WebListener // 声明这个是一个监听器
public class UserListener implements ServletContextListener { // ServletContextListener 类 能够监听 ServletContext 的生命周期，实际上就是监听 web 的生命周期

    @Override
    public void contextInitialized(ServletContextEvent sce) { // contextlnitialized Servlet容器启动时的方法
        System.out.println("ServletContext  初始化");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) { // contextDestroyed 终止web应用时调用的方法
        System.out.println("ServletContext  销毁");
    }

}
