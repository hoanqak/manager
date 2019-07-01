package com.manager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "leave_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "total_leave_day")
    private int totalLeaveDay;
    @Column(name = "remain_leave_day")
    private int remainLeaveDay;
    @Column(name = "updated_time")
    @UpdateTimestamp
    private Date updatedTime;
    @ManyToOne
    @JoinColumn(name = "id_user", unique = true)
    private User user;

    public LeaveData(int totalLeaveDay, int remainLeaveDay, Date updatedTime, User user) {
        this.totalLeaveDay = totalLeaveDay;
        this.remainLeaveDay = remainLeaveDay;
        this.updatedTime = updatedTime;
        this.user = user;
    }
}
