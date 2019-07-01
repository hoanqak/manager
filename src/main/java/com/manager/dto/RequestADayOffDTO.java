package com.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestADayOffDTO {

    private long fromDate;
    private long toDate;
    private String reason;
    private int totalDayOff;
    private int remainDayOff;

}
