package com.school.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class EmailUtil {

	private static Logger logger = Logger.getLogger(EmailUtil.class);
	private static Properties prop = new Properties();
	private static final String host = "mail.smtp.host";
	private static final String socketFactoryPort = "mail.smtp.socketFactory.port";
	private static final String socketFactoryClass = "mail.smtp.socketFactory.class";
	private static final String authFlag = "mail.smtp.auth";
	private static final String smtpPort = "mail.smtp.port";
	private static final String userName = "user.name";
	private static final String userPass = "user.password";
	private static final String userSender = "user.sender";
	private static String loginName;
	private static String loginPass;
	private static String senderAdrs;
	private static Session session;

	static {
		try {
			loadProperties(prop);
			loginName = prop.getProperty(userName, "mail.schoolutions@gmail.com");
			loginPass = prop.getProperty(userPass);
			senderAdrs = prop.getProperty(userSender, "mail.schoolutions@yourschool.com");
			configureMail();
		} catch (IOException exception) {
			logger.error("STATUS CODE: " + StatusCode.INTERNAL_ERROR
					+ " .Exception occured while loading mail.properties file. Using default values");
		}
	}

	public int emailOTP(String recepient, String OTP) {
		String OTPSubject = "Verification Code";
		String OTPMessage = " is the OTP to authenticate the password update process.";
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(senderAdrs, "Schoolutions"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recepient));

			message.setSubject(OTPSubject);
			message.setContent("<strong>" + OTP + "</strong>" + OTPMessage, "text/html");

			Transport.send(message);
			logger.info("OTP emailed to '" + recepient + "'");
			return StatusCode.OK;
		} catch (MessagingException | UnsupportedEncodingException exception) {
			logger.error("STATUS CODE: " + StatusCode.EMAILERROR + ": Exception occured while sending the OTP to "
					+ recepient + "\n" + exception);
			return StatusCode.EMAILERROR;
		}
	}

	public int sendEmail(String recepient, String messageBody, String subject, DataHandler[] attachments) {
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(senderAdrs, "Schoolutions"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recepient));
			message.setSubject(subject);

			Multipart multiPart = new MimeMultipart();

			BodyPart msgBody = new MimeBodyPart();
			msgBody.setText(messageBody);

			if (attachments != null) {
				for (DataHandler dh : attachments) {
					BodyPart attachment = new MimeBodyPart();
					attachment.setDataHandler(dh);
					attachment.setFileName(dh.getName());
					multiPart.addBodyPart(attachment);
				}
			}

			message.setContent(multiPart);
			Transport.send(message);
			logger.info("Email has been sent to '" + recepient + "'");
			return StatusCode.OK;
		} catch (MessagingException | UnsupportedEncodingException exception) {
			logger.error("STATUS CODE: " + StatusCode.EMAILERROR + ": Exception occured while sending the email to "
					+ recepient + "\n" + exception);
			return StatusCode.EMAILERROR;
		}
	}

	public String[] broadcastEmail(String[] recepientList, String messageBody, String subject,
			DataHandler[] attachments) {

		int failedEmails = 0;
		String[] failedRecepientList = new String[recepientList.length];

		Message message = new MimeMessage(session);

		for (String recepient : recepientList) {
			try {
				message.setFrom(new InternetAddress(senderAdrs, "Schoolutions"));

				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recepient));
				message.setSubject(subject);

				Multipart multiPart = new MimeMultipart();

				BodyPart msgBody = new MimeBodyPart();
				msgBody.setText(messageBody);
				multiPart.addBodyPart(msgBody);

				if (attachments != null) {
					for (DataHandler dh : attachments) {
						BodyPart attachment = new MimeBodyPart();
						attachment.setDataHandler(dh);
						attachment.setFileName(dh.getName());
						multiPart.addBodyPart(attachment);
					}
				}

				message.setContent(multiPart);
				Transport.send(message);
				logger.info("Email has been sent to '" + recepient + "'");

			} catch (MessagingException | UnsupportedEncodingException exception) {
				logger.error("STATUS CODE: " + StatusCode.EMAILERROR + ": Exception occured while sending the OTP to "
						+ recepient + "\n" + exception);
				failedRecepientList[failedEmails++] = recepient;
			}
		}
		return failedRecepientList;
	}

	private static void configureMail() {
		session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(loginName, loginPass);
			}
		});
	}

	private static void loadProperties(Properties prop) throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try (InputStream is = classLoader.getResourceAsStream("mail.properties")) {
			if (is != null)
				prop.load(is);
			else
				logger.error("STATUS CODE: " + StatusCode.INTERNAL_ERROR
						+ " .Exception occured while loading mail.properties file.Using default values.");

		} catch (IOException exception) {
			logger.error("STATUS CODE: " + StatusCode.INTERNAL_ERROR
					+ " .Exception occured while loading mail.properties file.\n" + exception);
			throw new IOException();
		}

		prop.put(host, prop.getProperty(host, "smtp.gmail.com"));
		prop.put(socketFactoryPort, prop.getProperty(socketFactoryPort, "465"));
		prop.put(socketFactoryClass, prop.getProperty(socketFactoryClass, "javax.net.ssl.SSLSocketFactory"));
		prop.put(authFlag, prop.getProperty(authFlag, "true"));
		prop.put(smtpPort, prop.getProperty(smtpPort, "465"));

	}

	public static void main(String[] args) {
		File f1 = new File("C:\\Users\\Home\\Desktop\\1.jpg");
		File f2 = new File("C:\\Users\\Home\\Desktop\\CREATE_TABLE.sql");
		File f3 = new File("C:\\Users\\Home\\Desktop\\possuite-141-dm-ddl.rar");

		DataHandler dh1 = new DataHandler(new FileDataSource(f1));
		DataHandler dh2 = new DataHandler(new FileDataSource(f2));
		DataHandler dh3 = new DataHandler(new FileDataSource(f3));

		DataHandler[] dh = new DataHandler[] { dh1, dh2, dh3 };
		String[] recepientList = new String[] { "aakash.gupta140@gmail.com", "aakash.gupta140@outlook.com",
				"vikas.gupta0502@outlook.com", "pkgn1965@gmail.com" };

		String[] s = new EmailUtil().broadcastEmail(recepientList, "Hello!!", "Announcement", dh);
		for (String ss : s) {
			System.out.println(ss);
		}
	}
}
