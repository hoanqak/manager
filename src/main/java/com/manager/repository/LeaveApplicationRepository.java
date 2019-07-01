package com.manager.repository;

import com.manager.model.LeaveApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Integer> {

    @Query(nativeQuery = true, value = "select * from leave_application as leaveApplication where MONTH(leaveApplication.created_time) = :mon and leaveApplication.id_user=:userId")
    public List<LeaveApplication> getListApplicationInWeek(@Param("mon") int month, @Param("userId") int userId);

}
