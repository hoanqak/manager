package com.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class ResetPasswordDTO {

    @NotNull
    @Email
    private String email;
    @Size(max = 15, min = 8)
    private String oldPassword;
    private String newPassword;
    private String newPassword1;

}
