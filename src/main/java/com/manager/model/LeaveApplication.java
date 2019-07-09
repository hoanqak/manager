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
@Table(name = "leave_application")
@AllArgsConstructor
@NoArgsConstructor
public class LeaveApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    @Column
    private String reason;
    private String status;
    @CreationTimestamp
    @Column(name = "created_time")
    private Date createdTime;
    @Column(name = "updated_time")
    @UpdateTimestamp
    private Date updatedTime;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

}
