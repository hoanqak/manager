package com.manager.model;

import lombok.*;
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
@ToString

public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "fullname")
	private String name;

	@Column(name = "birthday")
	private Date birthday;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "address")
	private String address;

	private String password;

	@Column(name = "phone", length = 10)
	private String phoneNumber;

	@Column
	private String picture;

	@Column(name = "kind_of_emp")
	private String kindOfEmployee;

	@Column(name = "created_time")
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "updated_time")
	@UpdateTimestamp

	private Date updatedDate;

	@Column
	private int status;
	private int role;
	private int department;
	private int position;

}
