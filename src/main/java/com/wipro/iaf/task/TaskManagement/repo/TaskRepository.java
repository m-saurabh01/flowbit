package com.wipro.iaf.task.TaskManagement.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wipro.iaf.task.TaskManagement.entity.Task;
import com.wipro.iaf.task.TaskManagement.entity.User;
import com.wipro.iaf.task.TaskManagement.enums.Status;

public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findByStatus(Status valueOf);

	List<Task> findByDeadlineBeforeAndStatusNot(LocalDate now, Status done);
	List<Task> findByDeadlineBeforeAndStatusNotIn(LocalDate date, List<Status> statuses);
	
	List<Task> findByCreatedByAndDeadlineBeforeAndStatusNotIn(Long managerId, LocalDate date, List<Status> statuses);



	List<Task> findByDeadlineBetween(LocalDate now, LocalDate plusDays);

	long countByStatus(Status status);
	
	@Query("SELECT COUNT(t) FROM Task t JOIN t.assignedUsers u WHERE u.id = :userId AND t.status = :status")
	long countByStatusAndUserId(@Param("status") Status status, @Param("userId") Long userId);
	
	@Query("SELECT COUNT(t) FROM Task t WHERE t.status = :status AND t.createdBy = :managerId")
	long countByStatusAndManagerId(@Param("status") Status status, @Param("managerId") Long managerId);

	@Query("SELECT t FROM Task t JOIN t.assignedUsers u WHERE u = :user")
	List<Task> findAllByAssignedUsersContains(@Param("user") User user);

	@EntityGraph(value = "Task.detailView", type = EntityGraph.EntityGraphType.LOAD)
	Optional<Task> findWithDetailsById(Long id);

	List<Task> findByAssignedUsersContaining(User user);

	List<Task> findByCreatedByAndStatus(Long id, Status inReview);

	List<Task> findByCreatedByAndDeadlineBeforeAndStatusNot(Long id, LocalDate today, Status done);

	List<Task> findByAssignedUsersContainingAndCreatedBy(User employee, Long managerId);

	List<Task> findAllByCreatedBy(Long long1); 
}

