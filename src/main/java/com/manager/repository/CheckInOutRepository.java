package com.manager.repository;

import com.manager.model.CheckInOut;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CheckInOutRepository extends JpaRepository<CheckInOut, Integer> {

    @Query(nativeQuery = true, value = "select d.day_check_in from check_in_out as d order by id desc limit 0, 1")
    public Date getDate();
    @Query(nativeQuery = true, value = "SELECT * from check_in_out as checkInOut where checkInOut.day_check_in = :date")
    public CheckInOut getCheckInOutByDate(@Param("date") String date);

}
