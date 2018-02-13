package com.tpprod.stupor;

import java.io.File;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Crash extends Thread {
	@Override
	public void run() {
		if(StateMachine.running) {
			final String subject ="Crash Detected";
			final String emailBody = "A crash has occured on a system with the properties: \n";
			final String to = "stuporgame@gmail.com";
			final String from = "stuporgame@gmail.com";
			final String username = "stuporgame@gmail.com";
			final String password = "stuporgame2017";
	
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
	
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
	
			try {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
				message.setSubject(subject);
				Multipart multipart = new MimeMultipart();
				
				MimeBodyPart text = new MimeBodyPart();
				text.setText(emailBody + System.getProperties());
				multipart.addBodyPart(text);
				
				String[] log = new File("./Content/Logs").list();
				for(int i = 0; i < log.length; i++) {
					MimeBodyPart logs = new MimeBodyPart();
					logs.attachFile( "./Content/Logs/" + log[i]);
					multipart.addBodyPart(logs);
				
				}
				
				message.setContent(multipart);
				
				Transport.send(message);
				
				System.out.println("sent");
	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
