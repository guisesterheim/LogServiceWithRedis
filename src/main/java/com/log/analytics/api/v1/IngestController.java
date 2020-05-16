package com.log.analytics.api.v1;

import com.log.analytics.com.log.analytics.entity.Log;
import com.log.analytics.com.log.analytics.entity.Metrics;
import com.log.analytics.com.log.analytics.entity.SingleMetric;
import com.log.analytics.enums.RegionEnum;
import com.log.analytics.exceptions.InvalidIngestionInputException;
import com.log.analytics.exceptions.NoDataFoundException;
import com.log.analytics.service.LogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/laa")
public class IngestController {

    private static final String NO_DATA_WAS_PARSED = "No data was parsed";
    private static final String ERROR_APP_IS_DOWN = "Error! App is down";

    @Autowired
    private LogsService logService;

    @GetMapping("/healthCheck")
    public ResponseEntity healthCheck() {
        if(logService.healthCheck())
            return ResponseEntity.ok("OK");

        return ResponseEntity.badRequest().body(ERROR_APP_IS_DOWN);
    }

    @GetMapping("/metrics")
    public ResponseEntity<Metrics> getMetrics() {
        try {
            Metrics ret = new Metrics();
            ret.setMostAccessedURLsWorldwide(logService.findMostURLsAccessed());
            ret.setLessAccessedURLWorldWide(logService.findLeastURLAccessed());
            ret.setMostAccessedMinute(logService.findMostAccessedPerMinute());
            ret.setTopThreeAccessPerDay(logService.findMostAccessedPerDay());
            ret.setTopThreeAccessPerWeek(logService.findMostAccessedPerWeek());
            ret.setTopThreeAccessPerYear(logService.findMostAccessedPerYear());
            ret.setMostAccessedURLPerRegion(logService.findMostURLsAccessedPerRegion(RegionEnum.USWEST2));

            return ResponseEntity.ok(ret);
        }catch(NoDataFoundException nex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity ingest(@RequestBody List<Log> json) throws InvalidIngestionInputException{
        try {
            checkIngestionInput(json);

            logService.ingestLogs(json);

            return ResponseEntity.ok("Items ingested succesfully");
        }catch(InvalidIngestionInputException iex){
            return ResponseEntity.badRequest().body(iex.getMessage());
        }
    }

    private void checkIngestionInput(List<Log> json) throws InvalidIngestionInputException{
        if(CollectionUtils.isEmpty(json))
            throw new InvalidIngestionInputException(InvalidIngestionInputException.INPUT_CANNOT_BE_EMPTY);
    }
}