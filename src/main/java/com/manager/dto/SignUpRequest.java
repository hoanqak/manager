package com.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

	@NotNull
	@NotBlank
	@Size(max = 100)
	private String name;

	@NotBlank
	@Email
	@Size(min = 3, max = 50)
	private String email;

	@NotBlank
	@Size(min = 8, max = 20)
	private String password;

	@NotNull
	private int position;

}
