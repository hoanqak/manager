package com.manager.model;

import com.manager.dto.UserDTO;
import com.manager.md5.MD5;
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
    @Column(name="birth_day")
    private Date birthDay;
    @Column(name="email", unique = true)
    private String email;
    @Column(name="address")
    private String address;
    @Column
    private String password;
    @Column(name="phone_number", length = 10)
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

    //	khởi tạo User từ UserDTO
    public static User adminCreateUser(UserDTO userDTO) {
        User user = new User();
	    MD5 md5 = new MD5();
	    String passwordDefault = md5.convertToMD5(String.valueOf(12345678));
	    user.setPassword(passwordDefault);
        user.setName(userDTO.getName());
        user.setBirthDay(new Date(userDTO.getBirthday()));
        user.setPosition(userDTO.getPosition());
        user.setPhoneNumber(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());

        return user;
    }
}
