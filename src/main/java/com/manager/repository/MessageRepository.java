package com.manager.repository;

import com.manager.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select m from Message as m where m.status=:status")
	List<Message> getListMessageByStatus(@Param("status") boolean status);

	Message getMessageById(int id);

}
