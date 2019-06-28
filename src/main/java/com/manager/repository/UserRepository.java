package com.manager.repository;

import com.manager.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	@Query("SELECT u.email FROM User as u where u.email = :email")
	public List<String> findEmail(@Param("email") String email);
	@Query("SELECT u.password FROM User as u where u.email = :email")
	public List<String> findPassword(@Param("email") String email);
	@Query ("SELECT u.id FROM User as u where u.email = :email ")
	public Integer findIdByEmail(@Param("email") String email);
	@Query("select u from User as u where u.email = :email")
	public User searchUserByEmail(@Param("email") String email);
}
