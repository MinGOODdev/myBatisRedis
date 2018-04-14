package mybatis.hackday.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class CacheManagerCheck implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(CacheManagerCheck.class);
    private final CacheManager cacheManager;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public CacheManagerCheck(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void run(String... strings) throws Exception {
        logger.info("\n\n" + "===========================================================================\n"
        + "Using cache manager: " + this.cacheManager.getClass().getName() + "\n"
        + "===========================================================================\n\n");
    }

}
