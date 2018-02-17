package com.tpprod.stupor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Crash extends Thread {

	private JFrame Crash = new JFrame("Crash Report");

	@Override
	public void run() {
		/*
		 * This code runs if the game exits, be it a regular exit or a crash
		 * 
		 * This if statement checks if the StateMachine is currently running, if so the
		 * game crashed in its normal coarse of operation and an error should be reported
		 * The if statement also checks if the user was pressing the cmd or alt
		 * key, if so they terminated the game out of coarse and a crash report should
		 * not be generated
		 */
		if (StateMachine.isRunning() && StateMachine.getCurrentKeys().indexOf(157) == -1 /* cmd key */
				&& StateMachine.getCurrentKeys().indexOf(18) == -1 /* alt key */) {
			/*
			 * We start the crash report by making a separate JPanel that will pop up asking
			 * if the user would like to send an email back to the developers that details
			 * the error that caused the crash. Below is the creation of that panel.
			 */
			JPanel panel = new JPanel();
			panel.setBounds(0, 0, 500, 400);
			panel.setBackground(Color.GRAY);

			Crash.setLayout(new GridLayout());

			String[] errors = new File("./Content/Logs").list();
			String error = errors[errors.length-1];
			String errorMessage = "Error not found";
			try {
				errorMessage = new Scanner(new File("./Content/Logs/" + error)).useDelimiter("\\Z").next();
				errorMessage = errorMessage.substring(errorMessage.lastIndexOf("-")-16, errorMessage.length());
				errorMessage = errorMessage.replace("\t", "<br>");
			} catch (Exception e) {
				
			} 
			error = error.substring(error.lastIndexOf("-")-16, error.length());
			JLabel label = new JLabel(
					"<html>An unhandled error has occured in your system: <br><br>" + errorMessage + "<br><br> If you click 'Send' a report will be sent to the developers<br>detailing the problem so that they may attemt to fix it<br> If you hit 'Don't Send' the program will exit</html>");
			label.setFont(new Font("TimesNewRoman", 0, 14));
			label.setForeground(Color.BLACK);

			panel.add(label, BorderLayout.BEFORE_FIRST_LINE);

			JButton b1 = new JButton("Button 1");
			b1.setBounds(50, 300, 100, 50);
			b1.setBackground(new Color(200, 200, 200));
			b1.setText("Don't Send");

			JButton b2 = new JButton("Button 2");
			b2.setBounds(250, 300, 100, 50);
			b2.setBackground(new Color(200, 200, 200));
			b2.setText("Send");

			panel.add(b1, BorderLayout.SOUTH);
			panel.add(b2, BorderLayout.SOUTH);

			b1.setEnabled(true);
			b2.setEnabled(true);

			/*
			 * here we add the ActionListeners for the buttons so that if the user decides
			 * that they do not want to send an email we respect their wishes and exit, if
			 * they decide to help we send and email
			 */
			b1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					exit();
				}
			});
			b2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sendEmail();
				}
			});

			Crash.add(panel);
			Crash.setResizable(false);
			Crash.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			Crash.setSize(500, 400);
			Crash.setLayout(null);
			Crash.setVisible(true);
		}
	}

	private void sendEmail() {
		/*
		 * Here we send the crash report from us to us using javax.mail and
		 * javax.activation from a email we setup specifically for the game with gmail
		 */
		final String subject = "Crash Detected";
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
			/*
			 * Here we add the contents to the email, including system properties and log
			 * files, and send the email
			 */
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);

			Multipart multipart = new MimeMultipart();

			MimeBodyPart text = new MimeBodyPart();
			text.setText(emailBody + System.getProperties());
			multipart.addBodyPart(text);

			String[] log = new File("./Content/Logs").list();
			for (int i = 0; i < log.length; i++) {
				MimeBodyPart logs = new MimeBodyPart();
				logs.attachFile("./Content/Logs/" + log[i]);
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
		/*
		 * This closes the crash reporting panel after we are done with it
		 */
		Crash.setVisible(false);
		Crash.dispose();
	}
}
