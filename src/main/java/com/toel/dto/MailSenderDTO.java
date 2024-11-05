package com.toel.dto;

import java.io.File;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailSenderDTO {
	private String from = "khaduong28052004@gmail.com";
	private String toEmail;
	private String subject;
	private String content;
	private List<String> cc;
	private List<String> bcc;
	private List<File> files;

	public MailSenderDTO(String toEmail, String subject, String content) {
		this.toEmail = toEmail;
		this.subject = subject;
		this.content = content;
	}

}
