package com.manager.dto;

import com.manager.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDemoDTO {
    private int id;
    private boolean status;
    private String message;
    private String type;
    private long createdTime;
    private String content;
    private String from;
    private String to;
}
