package com.manager.repository;

import com.manager.model.LeaveApplication;
import com.manager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Integer> {
	@Query(nativeQuery = true, value = "select * from leave_application as leaveApplication where MONTH(leaveApplication.created_time) = :mon and leaveApplication.id_user=:userId")
	List<LeaveApplication> getListApplicationInWeek(@Param("mon") int month, @Param("userId") int userId);
	@Query("select leaveApplication from LeaveApplication leaveApplication where leaveApplication.user = :user")
	Page<LeaveApplication> getLeaveApplicationByPage(Pageable pageable,@Param("user") User user);
	@Query("select leaveApplication from LeaveApplication leaveApplication where leaveApplication.id=:id")
	LeaveApplication getLeaveApplicationsById(@Param("id") int id);
}
