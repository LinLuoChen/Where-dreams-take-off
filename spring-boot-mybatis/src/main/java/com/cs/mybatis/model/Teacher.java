/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 */

package com.cs.mybatis.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name="teacher") // 数据库名对应
public class Teacher implements Serializable {

    // 序列化
	private static final long serialVersionUID = 1L;
	/**
	 * ID
	 */
	@Id // 声明主键
	@Column(name = "id") // 与数据库字段名对应
	private Integer id;
	/**
	 * 名字
	 */
	@Column(name = "name")
	private String name;
	/**
	 * 年龄
	 */
	@Column(name = "age")
	private Integer age;

	public Teacher(){
	}

	public Teacher(
		Integer id
	){
		this.id = id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
	public Integer getAge() {
		return this.age;
	}

}

