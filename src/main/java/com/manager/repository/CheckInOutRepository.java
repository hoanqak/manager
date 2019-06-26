package com.manager.repository;

import com.manager.model.CheckInOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckInOutRepository extends JpaRepository<CheckInOut, Integer> {
}
