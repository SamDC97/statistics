package be.exam.statistics.domain.repository;

import be.exam.statistics.domain.DriverStatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverStatisticsRepository extends JpaRepository<DriverStatisticsEntity, Long> {
}
