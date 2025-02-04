package net.duchung.quora.common.security;

import lombok.AllArgsConstructor;
import net.duchung.quora.common.security.jwt.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtBlacklistService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtUtil jwtUtil;

    public void addToBlackList(String token) {
        redisTemplate.opsForValue().set(token, "blacklist", jwtUtil.getExpirationTime()/1000);
    }

    public boolean isBlackListed(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
