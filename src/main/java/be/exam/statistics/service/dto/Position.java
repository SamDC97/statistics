package be.exam.statistics.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Position {

    private Long driverId;
    private Long points;

    private Driver driver;
}
