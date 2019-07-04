package com.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDemoDTO {
	private int id;
	private boolean status;
	private String message;
	private String type;
	private long timeRequest;
	private String content;
	private String from;
	private String to;
}
