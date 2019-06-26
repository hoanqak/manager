package com.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="status")
@AllArgsConstructor
@NoArgsConstructor
public class Status {

    @Id
    @GeneratedValue
    private int id;
    @Column
    private String status;
    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    List<User> listUser;
    public Status(String status) {
        this.status = status;
    }
}
