package com.manager.model;

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

    public User(String name, Date birthDay, String email, String address, String password, String phoneNumber, String picture, String kindOfEmployee) {
        this.name = name;
        this.birthDay = birthDay;
        this.email = email;
        this.address = address;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.picture = picture;
        this.kindOfEmployee = kindOfEmployee;
    }
}
