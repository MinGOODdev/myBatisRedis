package mybatis.hackday.service;

import mybatis.hackday.dto.Post;
import mybatis.hackday.repository.RedisPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
public class RedisPostService implements RedisPostRepository {

  private static final String KEY = "Post";
  private RedisTemplate<String, Post> redisTemplate;
  private HashOperations<String, String, Post> hashOperations;

  @Autowired
  public RedisPostService(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @PostConstruct
  private void init() {
    hashOperations = redisTemplate.opsForHash();
  }

  @Override
  public Map<String, Post> findAllPost() {
    return hashOperations.entries(KEY);
  }

  /**
   * Redisp에 조회수가 10 이상인 게시글을 저장합니다.
   * (인기 게시글을 빠르게 조회하기 위한 설정입니다.)
   * @param posts
   */
  @Override
  public void savePost(List<Post> posts) {
    redisTemplate.execute(RedisConnection::serverCommands).flushAll();

    for (Post p : posts) {
      StringBuilder builder = new StringBuilder();
      builder.append(p.getId());
      if (p.getHit() >= 10)
        hashOperations.put(KEY, builder.toString(), p);
    }
  }

}
