package net.duchung.quora.service.impl;

import net.duchung.quora.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public void saveSuggest(Long userId, Map<Long, Double> suggest) {

        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        String key = "user:" + userId + ":scores";

        suggest.forEach((k, v) -> zSetOperations.add(key, k, v));
    }
    public Set<ZSetOperations.TypedTuple<Object>> getUserScoresSorted(Long userId) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        String key = "user:" + userId + ":scores";
        Set<ZSetOperations.TypedTuple<Object>> topElements =  zSetOperations.reverseRangeWithScores(key, 0, 9);



        // Iterate through the set and set the score of each element to -1
        assert topElements != null;
        for (ZSetOperations.TypedTuple<Object> element : topElements) {
            Object value = element.getValue();
            zSetOperations.add(key, value, -1); // Update the score to -1
        }
        return topElements;

    }
}
