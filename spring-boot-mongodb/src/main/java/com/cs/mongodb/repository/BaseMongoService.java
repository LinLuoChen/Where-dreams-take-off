package com.cs.mongodb.repository;

import com.cs.mongodb.utils.UpdateQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * 
 * @ClassName BaseMongoService
 * @Description 
 * @author Alex
 * @Date 2019-03-29 16:17:29
 * @version 1.0.0
 * @param <T>
 */
public class BaseMongoService<T> {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	/**
	 * 
	 * @Description 插入对象
	 * @param mongo mongo对象
	 * @return
	 * Create at: 2019-03-29 16:10:55
	 * @author: Alex
	 * Revision:
	 *    2019-03-29 16:10:55 - first revision by Alex
	 *
	 */
	public String insert(T mongo) {
		try {
			mongoTemplate.save(mongo);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 
	 * @Description 设置id
	 * @param id
	 * @return
	 * Create at: 2019-03-29 16:12:02
	 * @author: Alex
	 * Revision:
	 *    2019-03-29 16:12:02 - first revision by Alex
	 *
	 */
	public Query setId(String id) {
		Query query = new Query(Criteria.where("_id").is(id));
		return query;
	}
	
	/**
	 * 
	 * @Description 获取UpdateQuery
	 * @return 
	 * Create at: 2019-03-29 16:12:33
	 * @author: Alex
	 * Revision:
	 *    2019-03-29 16:12:33 - first revision by Alex
	 *
	 */
	public UpdateQuery getUpdateQuery() {
		return new UpdateQuery();
	}
	
	/**
	 * 
	 * @Description 根据id更新
	 * @param query setId的query
	 * @param up update对象
	 * @param entityClass 对象class
	 * @return
	 * Create at: 2019-03-29 16:13:09
	 * @author: Alex
	 * Revision:
	 *    2019-03-29 16:13:09 - first revision by Alex
	 *
	 */
	public String updateById(Query query,UpdateQuery up,Class<T> entityClass) {
		try {
			mongoTemplate.updateFirst(query, up.getUpdate(), entityClass);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 
	 * @Description 查询全部
	 * @param entityClass 对象class
	 * @return
	 * Create at: 2019-03-29 16:14:57
	 * @author: Alex
	 * Revision:
	 *    2019-03-29 16:14:57 - first revision by Alex
	 *
	 */
	public List<T> findAll(Class<T> entityClass) {
		return mongoTemplate.findAll(entityClass);
	}
	
	/**
	 * 
	 * @Description 根据id查询
	 * @param id 
	 * @param entityClass 对象class
	 * @return
	 * Create at: 2019-03-29 16:16:20
	 * @author: Alex
	 * Revision:
	 *    2019-03-29 16:16:20 - first revision by Alex
	 *
	 */
	public T selectById(String id,Class<T> entityClass) {
		Query query = new Query(Criteria.where("_id").is(id));
		return mongoTemplate.findOne(query, entityClass);
	}
	
	/**
	 * 
	 * @Description 根据Criteria查询
	 * @param criteria
	 * @param entityClass 对象class
	 * @return
	 * Create at: 2019-03-29 16:17:57
	 * @author: Alex
	 * Revision:
	 *    2019-03-29 16:17:57 - first revision by Alex
	 *
	 */
	public List<T> select(Criteria criteria,Class<T> entityClass) {
		Query query = new Query();
		query.addCriteria(criteria);
		return mongoTemplate.find(query, entityClass);
	}
	
	/**
	 * 
	 * @Description 删除对象
	 * @param mongo mongo对象
	 * @return
	 * Create at: 2019-03-29 16:18:45
	 * @author: Alex
	 * Revision:
	 *    2019-03-29 16:18:45 - first revision by Alex
	 *
	 */
	public String delete(T mongo) {
		try {
			mongoTemplate.remove(mongo);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 
	 * @Description 根据id删除
	 * @param id
	 * @param entityClass 对象class
	 * @return
	 * Create at: 2019-03-29 16:20:10
	 * @author: Alex
	 * Revision:
	 *    2019-03-29 16:20:10 - first revision by Alex
	 *
	 */
	public String deleteById(String id,Class<T> entityClass) {
		try {
			T mongo = selectById(id,entityClass);
			delete(mongo);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
}

