package net.duchung.quora.service.impl;

import net.duchung.quora.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private  Map<Long, Double> currentTopElements;
    private  Map<Long, Double> nextTopElements;

    public void saveSuggest(Long userId, Map<Long, Double> suggest) {


        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        String key = "user:" + userId + ":scores";


        redisTemplate.delete(key);

        suggest.forEach((k, v) -> zSetOperations.add(key, k, v));


    }

    @Override
    public Boolean isRunOutOfAvailableSuggest(Long userId) {
        return getNextTopElements(userId).containsValue(-1.0);
    }

    public Map<Long, Double> getUserScoresSorted(Long userId) {

        return getCurrentTopElements(userId);

    }
    public  Map<Long, Double> getCurrentTopElements(Long userId) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        String key = "user:" + userId + ":scores";
        Set<ZSetOperations.TypedTuple<Object>> topElements =  zSetOperations.reverseRangeWithScores(key, 0, 9);



        assert topElements != null;
        for (ZSetOperations.TypedTuple<Object> element : topElements) {
            Object value = element.getValue();
            zSetOperations.add(key, value, -1); // Update the score to -1
        }
        return topElements.stream().collect(Collectors.toMap(x -> (Long) x.getValue(), x -> x.getScore()));
    }
    public  Map<Long, Double> getNextTopElements(Long userId) {

        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        String key = "user:" + userId + ":scores";
        Set<ZSetOperations.TypedTuple<Object>> topElements =  zSetOperations.reverseRangeWithScores(key, 10, 19);

        assert topElements != null;
        for (ZSetOperations.TypedTuple<Object> element : topElements) {
            Object value = element.getValue();
            zSetOperations.add(key, value, -1); // Update the score to -1
        }
        return topElements.stream().collect(Collectors.toMap(x -> (Long) x.getValue(), x -> x.getScore()));
    }
}
