package com.manager.dto;

import java.util.Date;

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

	private int id;
	private String name;
	private long birthday;
	@Size(max = 15, min = 8)
	private String password;
	@Length(max = 10, min = 10)
	private String phone;
	@Email
	@NotNull
	private String email;
	private String address;
	private String picture;
	private String kindOfEmployee;
	private int status;
	private int role;
	private int position;
	private int department;
	private long createdDate;
	private long updatedDate;

	public UserDTO(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.birthday = user.getBirthDay().getTime();
		this.phone = user.getPhoneNumber();
		this.email = user.getEmail();
		this.status = user.getStatus();
		this.role = user.getRole();
		this.position = user.getPosition();
		this.createdDate = user.getCreatedDate().getTime();
		this.updatedDate = user.getUpdatedDate().getTime();
		this.picture = user.getPicture();
		this.address = user.getAddress();
		this.kindOfEmployee = user.getKindOfEmployee();

	}


}
