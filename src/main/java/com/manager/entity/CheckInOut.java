package com.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "check_in_out")
@AllArgsConstructor
@NoArgsConstructor
public class CheckInOut {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name="day_check_in")
    private Date dayCheckIn;
    @Column(name="start_time")
    private Date startTime;
    @Column(name="end_time")
    private Date endTime;
    @Column(name="updated_time")
    @UpdateTimestamp
    private Date updatedTime;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;


}
