package com.school.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class EmailUtil {

	private Logger logger = Logger.getLogger(this.getClass());
	private static final String host = "mail.smtp.host";
	private static final String socketFactoryPort = "mail.smtp.socketFactory.port";
	private static final String socketFactoryClass = "mail.smtp.socketFactory.class";
	private static final String authFlag = "mail.smtp.auth";
	private static final String smtpPort = "mail.smtp.port";
	private static final String userName = "user.name";
	private static final String userPass = "user.password";
	private static final String userSender = "user.sender";
	private static final String OTPSubject = "Verification Code";
	private static final String OTPMessage = " is the OTP to authenticate the password update process";

	public int emailOTP(String recepient, int OTP) {
		Properties prop = new Properties();
		try {
			loadProperties(prop);
		} catch (IOException exception) {
			return StatusCode.INTERNAL_ERROR;
		}
		String loginName = prop.getProperty(userName, "mail.schoolutions@gmail.com");
		String loginPass = prop.getProperty(userPass);
		String senderAdrs = prop.getProperty(userSender, "mail.schoolutions@yourschool.com");

		Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(loginName, loginPass);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderAdrs, "Schoolutions"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recepient));
			message.setSubject(OTPSubject);
			message.setContent("<strong>" + OTP + "</strong>" + OTPMessage, "text/html");
			Transport.send(message);
			logger.info("OTP emailed to '" + recepient + "'");
			return StatusCode.OK;
		} catch (MessagingException | UnsupportedEncodingException exception) {
			logger.error("STATUS CODE: " + StatusCode.EMAILERROR + ": Exception occured while sending an email.\n"
					+ exception);
			return StatusCode.EMAILERROR;
		}
	}

	public void loadProperties(Properties prop) throws IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try (InputStream is = classLoader.getResourceAsStream("mail.properties")) {
			prop.load(is);
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
		EmailUtil email = new EmailUtil();
		email.emailOTP("aakash.gupta140@outlook.com", 654879);
	}
}
