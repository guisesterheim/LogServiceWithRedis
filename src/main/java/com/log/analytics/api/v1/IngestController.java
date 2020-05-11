package com.log.analytics.api.v1;

import com.log.analytics.com.log.analytics.entity.Log;
import com.log.analytics.enums.RegionEnum;
import com.log.analytics.exceptions.InvalidIngestionInputException;
import com.log.analytics.exceptions.NoDataFoundException;
import com.log.analytics.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/laa")
public class IngestController {

    private static final String NO_DATA_WAS_PARSED = "No data was parsed";
    private static final String ERROR_APP_IS_DOWN = "Error! App is down";

    @Autowired
    private RedisService redisService;

    @GetMapping("/healthCheck")
    public ResponseEntity healthCheck() {
        if(redisService.healthCheck())
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().body(ERROR_APP_IS_DOWN);
    }

    // Check for dockerization = https://github.com/benweizhu/spring-redis-docker-example
    // Command to start redis = sudo docker run -d --name redis -p 6379:6379 redis


    @GetMapping("/metrics")
    public ResponseEntity<List<String>> getMetrics() {
        List<String> ret = new ArrayList<>();
        try {
            Map.Entry<String, Integer> mostCalledURL = redisService.findMostCalledURLAndCount();
            ret.add("Most called URL is: " + mostCalledURL.getKey() + ". And the count is: " + mostCalledURL.getValue());

            return ResponseEntity.ok(ret);
        }catch(NoDataFoundException nex){

            ret.add(NoDataFoundException.NO_DATA_FOUND);
            return ResponseEntity.badRequest().body(ret);
        }
    }

    @PostMapping("/")
    public ResponseEntity ingest(@RequestBody List<Log> json) throws InvalidIngestionInputException{
        try {
            checkIngestionInput(json);

            json.forEach(log -> {
                redisService.putURL(log.getUUID().toString(), log.getURL());
                redisService.putTimestamp(log.getUUID().toString(), String.valueOf(log.getTimestamp()));
                redisService.putRegion(log.getUUID().toString(), log.getRegion().toString());
            });

            return ResponseEntity.ok(json.size() +" items succesfully parsed");
        }catch(InvalidIngestionInputException iex){
            return ResponseEntity.badRequest().body(iex.getMessage());
        }
    }

    private void checkIngestionInput(List<Log> json) throws InvalidIngestionInputException{
        if(CollectionUtils.isEmpty(json))
            throw new InvalidIngestionInputException(InvalidIngestionInputException.INPUT_CANNOT_BE_EMPTY);
    }
}