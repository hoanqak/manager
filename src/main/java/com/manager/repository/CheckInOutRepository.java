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

    @Query(nativeQuery = true, value = "select d.day_check_in from check_in_out as d where d.id_user=:idUser order by id desc limit 0, 1")
    public Date getDate(@Param("idUser") int id);

    @Query(nativeQuery = true, value = "SELECT * from check_in_out as checkInOut where checkInOut.day_check_in = :date and checkInOut.id_user=:user")
    public CheckInOut getCheckInOutByDate(@Param("date") String date, @Param("user") int id);

    @Query("select c from  CheckInOut  as c where c.user.id=:id")
    public List<CheckInOut> getListCheckInOutByIdUser(@Param("id") int id);


	@Query("select c from CheckInOut c where c.dayCheckIn = :date")
	Page<CheckInOut> findCheckInOutsByDayCheckIn(Pageable pageable, @Param("date") Date date);

	@Query("select c from CheckInOut  c where c.dayCheckIn >= :startDate and c.dayCheckIn <= :endDate and c.user = :idUser")
	Page<CheckInOut> findCheckInOutsByDayCheckInAndAndUserId(Pageable pageable, @Param("startDate") Date startDate,
	                                                         @Param("endDate") Date endDate, @Param("idUser") int idUser);

}
