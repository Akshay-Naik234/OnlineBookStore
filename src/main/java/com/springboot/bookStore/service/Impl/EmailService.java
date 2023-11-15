package com.springboot.bookStore.service.Impl;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
	@Autowired
	private JavaMailSender sender;
	
	public boolean sendMail(String subject,String message,String to){
		MimeMessage mime = sender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mime,true);//true is the attachment
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			messageHelper.setText(message);
			sender.send(mime);
//			System.out.println("Message sent successfully");
			return true;
		}
		catch(MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void sendMailAttachment(String toAddress,String filePath){
		String EMAIL_SUBJECT = "Please Download your order invoice";
		String EMAIL_BODY = "Here is the Order Invoice for the online Book Purchase";
		MimeMessage message = sender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(message,true);//true is the attachment
			messageHelper.setTo(toAddress);
			messageHelper.setSubject(EMAIL_SUBJECT);
			messageHelper.setText(EMAIL_BODY);
			messageHelper.addAttachment("Online Book Store", new File(filePath));
			sender.send(message);
		}
		catch(MessagingException e) {
			System.out.println(e);
		}
		
	}
	
}
