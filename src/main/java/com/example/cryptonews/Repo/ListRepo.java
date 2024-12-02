/* package com.example.cryptonews.Repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.example.cryptonews.Util.Util;

@Repository
public class ListRepo {
    @Autowired
    @Qualifier(Util.listTemplate)
    RedisTemplate<String, String> template;

    // slide 30-34
    public void leftPush(String key, String Value) {
        template.opsForList().leftPush(key, Value);
    }

    public void rightPush(String Key, String Value) {
        template.opsForList().rightPush(Key, Value);
    }

    public void leftPop(String key, String Value) {
        template.opsForList().leftPop(key);
    }

    public void rightPop(String key, String Value) {
        template.opsForList().rightPop(key);
    }

    public String indexPosition(String key, Integer index) {
        return template.opsForList().index(key, index);
    }

    public Long listSize(String key) {
        return template.opsForList().size(key);
    }

    public List<String> getList(String key) {
        List<String> list = template.opsForList().range(key, 0, -1);
        return list;
    }

    public void remove(String key, Integer count, String value) {
        template.opsForList().remove(key, count, value);
    }

    public Boolean delete(String Key) {
        return template.delete(Key);
    }

}
 */