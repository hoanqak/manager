package com.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private int id;
    private long createdDate;
    private String message;
    private int idApplication;
    private String reason;
    private String status;
    private long startDate;
    private long endDate;
    private String name;


}
