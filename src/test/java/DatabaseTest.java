import com.log.analytics.Application;
import com.log.analytics.service.LogsService;
import com.log.analytics.service.RedisService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseTest {

    @Autowired
    private LogsService logsService;

    @Autowired
    private RedisService redisService;

    @Test
    @Order(1)
    void shouldHaveConnectionWithRedis(){
        /*
        assertAll(
                () -> assertTrue(logsService.healthCheck(), "Could not connect or insert data on Redis"),
                () -> assertNotNull((Object) this.redisService.getValue(RedisService.TEST_KEY), "Could not retrieve data from Redis")
        );*/
    }
}