package com.wipro.iaf.task.TaskManagement.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wipro.iaf.task.TaskManagement.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	List<User> findAllByActiveTrue();

	List<User> findByPendingManagerApprovalTrue();

	List<User> findByManagerAndActiveTrue(User manager);

	
	List<User> findByManagerIdAndActiveTrue(Long managerId);

	User findByEmailAndActiveTrue(String username);

	@Query("SELECT u FROM User u " +
		       "WHERE u.role ='MANAGER'" +
		       "AND u.id <> :userId " +
		       "AND (:managerId IS NULL OR u.id <> :managerId) " +
		       "AND (u.manager IS NULL OR u.manager.id <> :userId)")
		List<User> findCustomManagers(@Param("managerId") Long managerId, @Param("userId") Long userId);



}
