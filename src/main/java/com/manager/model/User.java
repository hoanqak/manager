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
    @Column(name = "fullname")
    private String name;
    @Column(name = "birth_day")
    private Date birthday;
    @Column(name = "email")
    private String email;
    @Column(name = "address")
    private String address;
    @Column(name = "password")
    private String password = "123456";
    @Column(name = "phone_number", length = 10)
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
    @Column(name = "department")
    private int department;

    //	khởi tạo User từ UserDTO
    public static User adminCreateUser(UserDTO userDTO) {
        User user = new User();

        user.setName(userDTO.getName());
        user.setBirthday(new Date(userDTO.getBirthday()));
        user.setPosition(userDTO.getPosition());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());

        return user;
    }

//    public User(UserDTO userDTO) {
//        this.id = userDTO.getId();
//        this.name = userDTO.getName();
//        this.address = userDTO.getAddress();
//        this.birthday = new Date(userDTO.getBirthday());
//        this.email = userDTO.getEmail();
//        this.phone = userDTO.getPhone();
//        this.picture = userDTO.getPicture();
//        this.department = userDTO.getDepartment();
//        this.role = userDTO.getRole();
//        this.status = userDTO.getStatus();
//        this.position = userDTO.getPosition();
//        this.kindOfEmployee = userDTO.getKindOfEmployee();
//        this.createdDate = new Date(userDTO.getCreatedDate());
//        this.updatedDate = new Date(userDTO.getUpdatedDate());
//
//    }

}
