package com.manager.repository;

import com.manager.model.LeaveData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveDataRepository extends JpaRepository<LeaveData, Integer> {
}
