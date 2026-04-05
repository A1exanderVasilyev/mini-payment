package dev.vasilyev.minipayment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.vasilyev.minipayment.api.dto.PaymentDto;
import dev.vasilyev.minipayment.api.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory connectionFactory,
            ObjectMapper objectMapper,
            @Value("${app.cache.users-ttl}") Duration usersTtl,
            @Value("${app.cache.payments-ttl}") Duration paymentsTtl
    ) {
        var usersSerializer = new Jackson2JsonRedisSerializer<>(UserDto.class);
        usersSerializer.setObjectMapper(objectMapper);
        RedisCacheConfiguration usersConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(usersTtl)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(usersSerializer));

        var paymentsSerializer = new Jackson2JsonRedisSerializer<>(PaymentDto.class);
        paymentsSerializer.setObjectMapper(objectMapper);
        RedisCacheConfiguration paymentsConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(paymentsTtl)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(paymentsSerializer));

        return RedisCacheManager.builder(connectionFactory)
                .withCacheConfiguration("users", usersConfig)
                .withCacheConfiguration("payments", paymentsConfig)
                .transactionAware()
                .build();
    }
}