package be.exam.statistics.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverStatisticsEntity {

    @Id
    @GeneratedValue
    private Long id;
    private List<PointEntity> positions;
    private List<RaceEntity> races;
}
