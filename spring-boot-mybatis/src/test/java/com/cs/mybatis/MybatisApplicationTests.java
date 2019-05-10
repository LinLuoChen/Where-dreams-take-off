package com.cs.mybatis;

import com.cs.mybatis.model.Teacher;
import com.cs.mybatis.service.TeacherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest // 声明这个是一个 TestController
public class MybatisApplicationTests {

    @Resource // 自动装配 Bean 用来调用你定义的方法
    TeacherService teacherService;

    @Test
    public void contextLoads() {
    }

    /**
     * @title: boot + mybatis selectAll()
     * @auther: linluochen
     * @date: 2019/5/5 10:41
     */
    @Test // 声明这个是个 TestClass
    public void TeacherSelectAll(){
        List<Teacher> teachersList = teacherService.selectAll(); // 获取实现类的方法
        for (Teacher teacher:teachersList) { // foreach 循环遍历
            System.out.println("ID："+teacher.getId()+" "+"姓名："+teacher.getName()+" "+"年龄："+teacher.getAge());
        }
    }

    /**
     * @title: boot + mybatis insertSelective()
     * @param: teacher
     * @auther: linluochen
     * @date: 2019/5/5 10:41
     */
    @Test // 声明这个是个 TestClass
    public void TeacherInsertSelective(){
        Teacher teacher = new Teacher(); // new 一个对象
        teacher.setName("小明"); // set 放值
        teacher.setAge(16); // set 放值
        Integer thy = teacherService.insertSelective(teacher); // 调用方法 成功返回 1  失败返回 0
        if(thy == 1){ // 进行判断
            System.out.println("成功");
        }else{
            System.out.println("失败");
        }
    }

}
