package com.manager.repository;

import com.manager.model.CheckInOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CheckInOutRepository extends JpaRepository<CheckInOut, Integer> {

	@Query("select c from CheckInOut c where c.dayCheckIn = :date")
	Page<CheckInOut> findCheckInOutsByDayCheckIn(Pageable pageable, @Param("date") Date date);

	@Query("select c from CheckInOut  c where c.dayCheckIn >= :startDate and c.dayCheckIn <= :endDate and c.user = :idUser")
	Page<CheckInOut> findCheckInOutsByDayCheckInAndAndUserId(Pageable pageable, @Param("startDate") Date startDate,
	                                                         @Param("endDate") Date endDate, @Param("idUser") int idUser);

}
