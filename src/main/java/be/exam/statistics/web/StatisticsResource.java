package be.exam.statistics.web;

import be.exam.statistics.service.StatisticsService;
import be.exam.statistics.service.dto.DriverResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StatisticsResource {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/driver-statistics")
    public ResponseEntity<List<DriverResult>> getAllDriverStatistics(){
        return new ResponseEntity<>(statisticsService.getDriverResults(), HttpStatus.OK);
    }
}
