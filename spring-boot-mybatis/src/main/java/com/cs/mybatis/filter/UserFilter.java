package com.cs.mybatis.filter;

/**
 * @ClassName UserFilter
 * @Param 过滤器
 * @Author linluochen
 * @Date 2019/4/23 12:02
 * @Version 1.0
 **/

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName ="UserFilter", urlPatterns ="/*") // 声明这个是一个过滤器
public class UserFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("----------->>> init"); // 初始化时调用 init
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("----------->>> doFilter");
        filterChain.doFilter(servletRequest,servletResponse) ; // 执行拦截时调用 doFilter
    }

    @Override
    public void destroy() {
        System.out.println("----------->>> destroy"); // 销毁连接时调用 destroy
    }
}
