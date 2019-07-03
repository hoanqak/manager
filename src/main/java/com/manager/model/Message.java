package com.manager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "status")
    private boolean status;
    @Column(name = "created_time")
    @CreationTimestamp
    private Date createdTime;
    @Column
    private String message;
    @ManyToOne
    @JoinColumn(name = "id_leave_application")
    private LeaveApplication leaveApplication;

    public Message(boolean status, LeaveApplication leaveApplication) {
        this.status = status;
        this.leaveApplication = leaveApplication;
    }
}
