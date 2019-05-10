package com.cs.mongodb.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @ClassName Mongo
 * @Param
 * @Author linluochen
 * @Date 2019/5/3 10:37
 * @Version 1.0
 **/
@Entity // 声明每个持久化 Popj 类都是一个实体Bean
@Table(name = "user") // 声明对象映射到数据库的数据表
public class Mongo implements Serializable {
    //序列化
    private static final long serialVersionUID = 1L; // serialVersionUID 用来表明类的不同版本间的兼容性

    //主键
    @Id // 指定主键
    private String id;

    private String name;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
