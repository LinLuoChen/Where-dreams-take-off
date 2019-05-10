/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 */

package com.cs.mybatis.mapper;

import com.cs.mybatis.base.BaseMapper;
import com.cs.mybatis.model.Teacher;
import org.springframework.stereotype.Component;

@Component
public interface TeacherMapper extends BaseMapper<Teacher> { // 这个地方我们调用的 Tk_MyBatis 持久层 API
}