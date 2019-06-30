package com.manager.model;

import com.manager.dto.UserDTO;
//import com.manager.service.StatusService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "birthday")
	private Date birthday;
	@Column(name = "email")
	private String email;
	@Column(name = "address")
	private String address;
	@Column(name = "password")
	private String password = "123456";
	@Column(name = "phone", length = 10)
	private String phone;
	@Column(name = "picture")
	private String picture;
	@Column(name = "kind_of_emp")
	private String kindOfEmployee;
	@Column(name = "status")
	private int status = 1;
	@Column(name = "role")
	private int role;
	@Column(name = "position")
	private int position;
	@Column(name = "created_time")
	@CreationTimestamp
	private Date createdDate;
	@Column(name = "updated_time")
	@UpdateTimestamp
	private Date updatedDate;

	//	khởi tạo User từ UserDTO
	public static User createUser(UserDTO userDTO) {
		User user = new User();

		user.setName(userDTO.getName());
		user.setBirthday(new Date(userDTO.getBirthday()));
		user.setPosition(userDTO.getPosition());
		user.setPhone(userDTO.getPhone());
		user.setEmail(userDTO.getEmail());
		user.setRole(userDTO.getRole());

		return user;
	}

}
