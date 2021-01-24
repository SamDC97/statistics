package be.exam.statistics.service;

import be.exam.statistics.domain.repository.DriverStatisticsRepository;
import be.exam.statistics.service.dto.*;
import be.exam.statistics.service.mapper.StatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StatisticsService {

    @Autowired
    private DriverStatisticsRepository driverStatisticsRepository;
    @Autowired
    private StatisticsMapper statisticsMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${url.drivers}")
    private String driversURL;
    @Value("${url.teams}")
    private String teamsURL;
    @Value("${url.races}")
    private String racesURL;
    @Value("${url.teamByDriver}")
    private String teamByDriverURL;

    public List<DriverResult> getDriverResults(){
        List<DriverResult> driverResults = new ArrayList<>();
        List<Driver> drivers = getRESTDrivers();
        List<Team> teams = getRESTTeams();

        for (Driver driver : drivers){
            driverResults.add(new DriverResult(driver, getTeamByDriver(driver.getId(), teams), getPointsForDriver(driver.getId())));
        }

        return driverResults;
    }

    private Long getPointsForDriver(Long driverId){
        Long points = 0L;
        List<Race> races = getRESTRaces();

        for (Race race : races){
            points += getPointsFromRace(race, driverId);
        }
        return points;
    }

    private Long getPointsFromRace(Race race, Long driverId){
        for (Position position : race.getPositions()){
            if (position.getDriverId() == driverId){
                return position.getPoints();
            }
        }
        return 0L;
    }

    private Team getTeamByDriver(Long id, List<Team> teams) {
        for (Team team : teams){
            if (team.getDriverId1() == id || team.getDriverId2() == id){
                return team;
            }
        }
        return null;
    }

    private List<Driver> getRESTDrivers() {
        ResponseEntity<List<Driver>> driverListEntity = restTemplate.exchange(driversURL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Driver>>() {
                }, Collections.emptyMap());
        if (driverListEntity.getStatusCode() == HttpStatus.OK) {
            return driverListEntity.getBody();
        }
        return null;
    }

    private List<Race> getRESTRaces() {
        ResponseEntity<List<Race>> raceListEntity = restTemplate.exchange(racesURL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Race>>() {
                }, Collections.emptyMap());
        if (raceListEntity.getStatusCode() == HttpStatus.OK) {
            return raceListEntity.getBody();
        }
        return null;
    }

    private List<Team> getRESTTeams() {
        ResponseEntity<List<Team>> teamListEntity = restTemplate.exchange(teamsURL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Team>>() {
                }, Collections.emptyMap());
        if (teamListEntity.getStatusCode() == HttpStatus.OK) {
            return teamListEntity.getBody();
        }
        return null;
    }
}
