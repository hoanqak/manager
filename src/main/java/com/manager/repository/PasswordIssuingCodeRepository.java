package com.manager.repository;

import com.manager.model.PasswordIssuingCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordIssuingCodeRepository extends JpaRepository<PasswordIssuingCode, Integer> {
    @Query("SELECT pass from PasswordIssuingCode as pass where id = :id")
    public PasswordIssuingCode getPasswordIssuingCodeById(@Param("id") int id);
}
