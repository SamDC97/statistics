package be.exam.statistics.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointEntity {

    @Id
    @GeneratedValue
    private Long id;
    private Long driverId;
    private Long teamId;
    private Long points;
}
