package com.manager.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String avatar;
    private int id;
    private String name;
    private String position;
    private String phone;
    private String email;
    private Date startDate;

}
