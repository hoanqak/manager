package com.manager.repository;

import com.manager.model.CheckInOut;
import com.manager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface CheckInOutRepository extends JpaRepository<CheckInOut, Integer> {

	@Query(nativeQuery = true, value = "select d.day_check_in from check_in_out as d where d.id_user=:idUser order by id desc limit 0, 1")
	Date getDate(@Param("idUser") int id);

	@Query(nativeQuery = true, value = "SELECT * from check_in_out as checkInOut where checkInOut.day_check_in = :date and checkInOut.id_user=:user")
	CheckInOut getCheckInOutByDate(@Param("date") String date, @Param("user") int id);

	@Query("select c from  CheckInOut  as c where c.user.id=:id")
	List<CheckInOut> getListCheckInOutByIdUser(@Param("id") int id);

	@Query("select c from CheckInOut c where c.dayCheckIn = :date")
	Page<CheckInOut> findCheckInOutsByDayCheckIn(Pageable pageable, @Param("date") Date date);

	@Query("select c from CheckInOut  c where c.dayCheckIn >= :startDate and c.dayCheckIn <= :endDate and c.user.id = :idUser")
	Page<CheckInOut> findCheckInOutsByDayCheckInAndUserId(Pageable pageable, @Param("startDate") Date startDate,
	                                                         @Param("endDate") Date endDate, @Param("idUser") int idUser);
	@Query("SELECT c FROM CheckInOut c where c.user=:user")
	Page<CheckInOut> getCheckInOutByUserAndPage(@Param("user") User user, Pageable pageable);

	CheckInOut getCheckInOutById(int id);

	@Query("select  c from CheckInOut c where c.dayCheckIn >= :startDate and c.dayCheckIn <= :endDate")
	List<CheckInOut> getListCheckInOutsByDayCheckIn(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
