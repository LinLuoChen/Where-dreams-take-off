package com.cs.druid.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName DruidConfiguration
 * @Param Druid  连接池配置
 * @Author linluochen
 * @Date 2019/4/22 16:39
 * @Version 1.0
 **/
@Configuration // 声明这个类是个 xml 配置文件
public class DruidConfiguration {

    @Bean //同等于 xml 文件中的 Bean 配置,Spring Boot 会把加上该注解的方法的返回值装进 Spring Ioc 的容器中
    public ServletRegistrationBean druidStatViewServlet() {
        //ServletRegistrationBean 提供类的进行注册
        ServletRegistrationBean servlet = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        //添加初始化参数
        //白名单：
        servlet.addInitParameter("allow","127.0.0.1");
        //IP黑名单（共存时，deny优先于allow）
        //如果满足deny,就会提示 Sorry,you are not permitted to view this page.servlet.addInitParameter("deny","192.168.1.73")
        //登录查看信息的账号密码
        servlet.addInitParameter("loginUsername","admin");
        servlet.addInitParameter("loginPassword","123456");
        //是否能重置数据
        servlet.addInitParameter("resetEnable","false");
        return servlet;
    }

    @Bean
    public FilterRegistrationBean druidStatFilter(){
        FilterRegistrationBean filter = new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则
        filter.addUrlPatterns("/*");
        //添加需要忽略的格式信息
        filter.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filter;
    }

}
