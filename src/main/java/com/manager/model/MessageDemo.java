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
    @Column(name="title")
    private String title;
    private int type;
    @Column
    private String content;
    @CreationTimestamp
    @Column(name = "time_request")
    private Date timeRequest;
    @ManyToOne
    @JoinColumn(name = "send_from")
    private User from;
    @Column(name = "id_report")
    private int idReport;
    @ManyToOne
    @JoinColumn(name = "send_to")
    private User to;

    public MessageDemo(String title, int type, String content, User from, User to) {
        this.title = title;
        this.type = type;
        this.content = content;
        this.from = from;
        this.to = to;
    }
}
