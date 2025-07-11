package com.wipro.iaf.task.TaskManagement.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wipro.iaf.task.TaskManagement.entity.DailyLog;

public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {

	
	@Query("SELECT l FROM DailyLog l WHERE l.task.id = :taskId")
	List<DailyLog> findLogsByTask(@Param("taskId") Long taskId);

	
}
