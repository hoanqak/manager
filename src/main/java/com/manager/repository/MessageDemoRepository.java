package com.manager.repository;

import com.manager.model.MessageDemo;
import com.manager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageDemoRepository extends JpaRepository<MessageDemo, Integer> {

	@Query("select message from MessageDemo message where message.status=:status and message.to=:user and message.type=:type")
	List<MessageDemo> getAllMessageByStatusAndToAndType(@Param("status") boolean status, @Param("user") User user,@Param("type") int type);

	@Query("select message from MessageDemo message where message.status=:status and message.to=:user")
	Page<MessageDemo> getAllMessageByStatusAndToAndTypePage(Pageable pageable, @Param("status") boolean status, @Param("user") User user);

	MessageDemo getMessageDemoByToAndId(User user, int id);
	Page<MessageDemo> getMessageDemoByTo(Pageable pageable, User user);
}
