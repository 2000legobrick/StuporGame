package com.tpprod.stupor;
/*
import java.io.File;
import java.util.Properties;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;  
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
*/
public class Crash extends Thread {
	private JFrame Crash = new JFrame("Crash Report");
	@Override
	public void run() {
		if(StateMachine.isRunning() && StateMachine.getCurrentKeys().indexOf(157) == -1 && StateMachine.getCurrentKeys().indexOf(18) == -1) {
			JPanel panel=new JPanel();  
			panel.setBounds(0,0,600,400);    
	        panel.setBackground(Color.GRAY); 
	        Crash.setLayout(new GridLayout());
	        JLabel label = new JLabel("<html>An unhandled error has occured in your system. <br> If you click 'Send' a report will be sent to the developers<br>detailing the problem so that they may attemt to fix it<br> If you hit 'Don' Send' the program will exit</html>");
	        label.setFont(new Font("TimesNewRoman",0, 20));
	        label.setForeground(Color.BLACK);
	        panel.add(label, BorderLayout.CENTER);
	        JButton b1=new JButton("Button 1");     
	        b1.setBounds(50,300,100,50);    
	        b1.setBackground(Color.darkGray); 
	        b1.setText("Don't Send");
	        JButton b2=new JButton("Button 2");     
	        b2.setBounds(250,300,100,50);    
	        b2.setBackground(Color.darkGray); 
	        b2.setText("Send");
	        panel.add(b1, BorderLayout.SOUTH);panel.add(b2, BorderLayout.SOUTH);
	        b1.setEnabled(true);b2.setEnabled(true);
	        b1.addActionListener(new ActionListener() { 
	        	  public void actionPerformed(ActionEvent e) { 
	        	    exit();
	        	  } 
	        	} );
	        b2.addActionListener(new ActionListener() { 
	        	  public void actionPerformed(ActionEvent e) { 
	        	    sendEmail();
	        	  } 
	        	} );
	        Crash.add(panel);
	        Crash.setResizable(false);
	        Crash.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        Crash.setSize(600,400);    
	        Crash.setLayout(null);    
	        Crash.setVisible(true); 
		}
	}
	private void sendEmail() {
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		exit();
	}
	private void exit() {
		Crash.setVisible(false);
		Crash.dispose();
	}
}
