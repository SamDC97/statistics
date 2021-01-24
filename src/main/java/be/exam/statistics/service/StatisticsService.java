package be.exam.statistics.service;

import be.exam.statistics.service.dto.*;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class StatisticsService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${url.drivers}")
    private String driversURL;
    @Value("${url.teams}")
    private String teamsURL;
    @Value("${url.races}")
    private String racesURL;

    public List<TeamResult> getTeamResults(){
        List<TeamResult> teamResults = new ArrayList<>();
        List<Team> teams = getRESTTeams();

        for (Team team : teams){
            teamResults.add(new TeamResult(team, getResultsForTeam(team)));
        }

        teamResults.sort(Comparator.comparing(TeamResult::getPoints).reversed());
        return teamResults;
    }

    public List<DriverResult> getDriverResults(){
        List<DriverResult> driverResults = new ArrayList<>();
        List<Driver> drivers = getRESTDrivers();
        List<Team> teams = getRESTTeams();

        for (Driver driver : drivers){
            driverResults.add(new DriverResult(driver, getTeamByDriver(driver.getId(), teams), getPointsForDriver(driver.getId())));
        }
        driverResults.sort(Comparator.comparing(DriverResult::getPoints).reversed());
        return driverResults;
    }

    private Long getResultsForTeam(Team team) {
        return getPointsForDriver(team.getDriverId1()) + getPointsForDriver(team.getDriverId2());
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
                log.info("Team {} gevonden voor Driver {}", team.getName(), id);
                return team;
            }
        }
        log.info("Geen team gevonden voor Driver met id: {}", id);
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
