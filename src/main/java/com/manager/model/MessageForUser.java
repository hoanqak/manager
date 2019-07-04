package com.manager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "message_for_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageForUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "message")
    private String message;
    @Column(name = "created_time")
    @CreationTimestamp
    private Date createdTime;
    @Column(name = "status")
    private boolean status;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "id_leave_application")
    private LeaveApplication leaveApplication;

    public MessageForUser(String message, boolean status, User user, LeaveApplication leaveApplication) {
        this.message = message;
        this.status = status;
        this.user = user;
        this.leaveApplication = leaveApplication;
    }
}
