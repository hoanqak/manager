package com.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
	@NotNull(message = "EMAIL_NOT_NULL")
	@Email(message = "TYPE_NOT_EMAIL")
	private String email;
	@Length(max = 15, min = 8)
	@NotNull(message = "PASSWORD_NOT_NULL")
	private String password;

}
