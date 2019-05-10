package com.cs.mongodb.utils;

import org.springframework.data.mongodb.core.query.Update;

/**
 * @ClassName UpdateQuery
 * @Param
 * @Author linluochen
 * @Date 2019/5/3 10:35
 * @Version 1.0
 **/
public class UpdateQuery {

    private Update update = new Update();

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public UpdateQuery setQuery(String property, Object value) {
        update.set(property, value);
        return (UpdateQuery) this;
    }
}
