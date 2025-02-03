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
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Saves a suggestion set (answer IDs and scores) into a Redis ZSET for a given user.
     */
    @Override
    public void saveSuggest(Long userId, Map<Long, Double> suggest) {
        String key = "user:" + userId + ":scores";
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();

        // Clear any existing ZSET for the user
        redisTemplate.delete(key);

        // Add new score entries
        suggest.forEach((answerId, score) -> zSetOperations.add(key, answerId, score));
    }

    /**
     * Simple check to see if the user’s available suggestions are “used up.”
     * This version counts items in the lower deciles or checks if key is missing.
     */
    @Override
    public Boolean isRunOutOfAvailableSuggest(Long userId) {
        // Adjusted logic: returns true only when the key is missing or all items have been viewed.
        // For demonstration, we check if the set is empty. Customize as needed.
        String key = "user:" + userId + ":scores";
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();

        // If key doesn't exist or the set is empty, it's “run out” (or was never created).
        Long size = zSetOperations.size(key);
        return (size == null || size == 0);
    }

    /**
     * Retrieves the top 10 item scores for this user from the ZSET, in descending order of scores.
     */
    @Override
    public Map<Long, Double> getUserScoresSorted(Long userId) {
        return getTopScores(userId, 0, 9);
    }

    /**
     * Helper to fetch items in a given range.
     */
    private Map<Long, Double> getTopScores(Long userId, long start, long end) {
        String key = "user:" + userId + ":scores";
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();

        // Retrieve the items in reverse (highest to lowest)
        Set<ZSetOperations.TypedTuple<Object>> rangeWithScores =
                zSetOperations.reverseRangeWithScores(key, start, end);

        // If nothing is returned, return an empty Map
        if (rangeWithScores == null) {
            return Map.of();
        }

        // Convert to Map<Long, Double>
        return rangeWithScores.stream()
                .collect(Collectors.toMap(
                        tuple -> (Long) tuple.getValue(),
                        ZSetOperations.TypedTuple::getScore
                ));
    }
}