package net.duchung.quora.service;

import net.duchung.quora.data.entity.User;
import net.duchung.quora.data.entity.View;
import net.duchung.quora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public interface RedisService {
    void saveSuggest(Long userId,Map<Long,Double> suggest);
    Set<ZSetOperations.TypedTuple<Object>> getUserScoresSorted(Long userId);


    }


