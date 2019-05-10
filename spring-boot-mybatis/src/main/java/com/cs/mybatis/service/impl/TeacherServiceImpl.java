/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 */

package com.cs.mybatis.service.impl;

import com.cs.mybatis.mapper.TeacherMapper;
import com.cs.mybatis.model.Teacher;
import com.cs.mybatis.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired // 自动装配 Bean
    TeacherMapper teacherMapper;

    /**
     * @title: 查询全部
     * @auther: linluochen
     * @date: 2019/5/5 10:25
     */
    @Override
    public List<Teacher> selectAll() {
        return teacherMapper.selectAll();
    }

    /**
     * @title: 新增
     * @param: teacher
     * @auther: linluochen
     * @date: 2019/5/5 10:25
     */
    @Override
    public Integer insertSelective(Teacher teacher) {
        return teacherMapper.insertSelective(teacher);
    }
}