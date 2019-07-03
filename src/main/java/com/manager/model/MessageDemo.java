package com.manager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message_demo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDemo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private boolean status;
    private String message;
    private String type;
    @CreationTimestamp
    @Column(name = "created_time")
    private Date createdTime;
    @Column
    private String content;
    @CreationTimestamp
    @Column(name = "time_request")
    private Date timeRequest;
    @ManyToOne
    @JoinColumn(name = "send_from")
    private User from;
    @Column(name = "id_leave_application")
    private int idLeaveApplication;
    @ManyToOne
    @JoinColumn(name = "send_to")
    private User to;

    public MessageDemo(String message, String type, String content, User from, User to) {
        this.message = message;
        this.type = type;
        this.content = content;
        this.from = from;
        this.to = to;
    }
}
