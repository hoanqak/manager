package com.manager.repository;

import com.manager.model.MessageForUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageForUserRepository extends JpaRepository<MessageForUser, Integer> {
}
