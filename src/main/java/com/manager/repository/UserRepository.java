package com.manager.repository;

import com.manager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Page<User> findUsersBy(Pageable pageable);

	User findUserByEmail(String email);

	User findUserById(int id);

	@Query("SELECT u.email FROM User as u where u.email = :email")
	String findEmail(@Param("email") String email);

	@Query("SELECT u.id FROM User as u where u.email = :email ")
	Integer findIdByEmail(@Param("email") String email);

	@Query("select u from User as u where u.email = :email")
	User searchUserByEmail(@Param("email") String email);

	@Query("select u from User as u where u.id = :id")
	User getUserById(@Param("id") int id);

	@Query("select u from User  as u where u.role=:role")
	List<User> getRoleUser(@Param("role") int role);

	//test
	@Query("select u from User as u")
	Page<User> findAll(Pageable pageable);
}
