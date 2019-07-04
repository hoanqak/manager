package com.manager.repository;

import com.manager.model.MessageDemo;
import com.manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageDemoRepository extends JpaRepository<MessageDemo, Integer> {
	MessageDemo getMessageDemoById(int id);

	List<MessageDemo> getAllMessageByStatus(boolean status);

	List<MessageDemo> getAllMessageByStatusAndTo(boolean status, User user);
}
