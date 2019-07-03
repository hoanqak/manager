package com.manager.dto;

import com.manager.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessageDTO {

    private String message;
    private int type;
    private String content;
    private User from;
    private User to;
    private long timeRequest;
    private int leaveApplication;

}
