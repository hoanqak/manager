package com.manager.dto;

import java.util.Date;

import com.manager.md5.MD5;
import lombok.AllArgsConstructor;
import com.manager.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NotNull
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class UserDTO {

	private final String DEFAULT_PASSWORD = "12345678";

	private int id;
	@NotNull
	private String name;
	@NotNull
	private long birthday;
	@Size(max = 15, min = 8)
	private String password = new MD5().convertToMD5(DEFAULT_PASSWORD);
	@Length(max = 13, min = 8)
	private String phone;
	@Email
	@NotNull
	private String email;
	private String address;
	private String picture;
	private String kindOfEmployee;
	private int status;
	private int role;
	@NotNull
	private int position;
	private int department;
	private long createdDate;
	private long updatedDate;
}
