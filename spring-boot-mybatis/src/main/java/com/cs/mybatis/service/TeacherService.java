/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 */

package com.cs.mybatis.service;

import com.cs.mybatis.model.Teacher;

import java.util.List;

public interface TeacherService{

    /**
     * @title: 查询全部
     * @auther: linluochen
     * @date: 2019/5/5 10:28
     */
	List<Teacher> selectAll();

	/**
     * @title:  增加
     * @param: teacher
     * @auther: linluochen
	 * @date: 2019/5/5 10:30
	 */
	Integer insertSelective(Teacher teacher);

}