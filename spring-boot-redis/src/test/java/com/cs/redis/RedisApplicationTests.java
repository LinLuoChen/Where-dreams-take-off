package com.cs.redis;

import com.cs.redis.config.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisApplicationTests {

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void contextLoads() {
    }

    @Test
    public void RedisTest(){
        Map<String,Object> map = new HashMap<String, Object>();
        String luochen = "linluochen"; // 定义它 Key 的名称
        String search = "小洛儿wqw"; // 定义它的 Value
        redisUtil.set(luochen,search); // 放进 redis 里
        String redis = (String) redisUtil.get(luochen); //取出值的时候直接去 Key 的名字就可以了，因为取出的值是object类型的所以需要给他强制转换一下
        System.out.println("结果为："+redis); //输出结果
        /*List<String> list = (List<String>) redisUtil.get(luochen);
        if(StringUtils.isNotBlank(search)) {
            if(list != null) {
                list.add(0,search);
                redisUtil.set(luochen, list,60L*60*24*30);
                map.put("historical",list.size()<=6?list:list.subList(0, 6));
            }else {
                List<String> searchList = new ArrayList<String>();
                searchList.add(search);
                redisUtil.set(luochen, searchList,60L*60*24*30);
                map.put("historical",searchList);
            }
            System.out.println("执行成功 ！！！！"+map); // 打印值
        }else {
            map.put("historical",list.size()<=6?list:list.subList(0, 6));
        }*/
    }

}
