package com.ecommerce.sistema.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SmtpEmailService extends AbstractEmailService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SmtpEmailService.class);
	@Autowired
	private MailSender mailSender;
	@Autowired
	private JavaMailSender javaMailSender;

	/**
	 * Liberar app menos seguro em: <a href =
	 * "https://myaccount.google.com/lesssecureapps?pli=1&rapt=AEjHL4PsS8rMpphE8PeG7TelQ5D0f7qr4Ka0wrcdeaeaYqGHH0yZ_3mCYCOeanGWODTb7g1GKx0Dlb4AOgtCOPVxMZ-Q5GoRIw"/>
	 * google link 1</a> e
	 * <a href="https://accounts.google.com/b/0/DisplayUnlockCaptcha"/> google link
	 * 2</a>
	 */
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOGGER.info("Enviando de Email de confirmação");
		LOGGER.info(msg.toString());
		mailSender.send(msg);
		LOGGER.info("Email enviado");

	}

	/**
	 * Liberar app menos seguro em: <a href =
	 * "https://myaccount.google.com/lesssecureapps?pli=1&rapt=AEjHL4PsS8rMpphE8PeG7TelQ5D0f7qr4Ka0wrcdeaeaYqGHH0yZ_3mCYCOeanGWODTb7g1GKx0Dlb4AOgtCOPVxMZ-Q5GoRIw"/>
	 * google link 1</a> e
	 * <a href="https://accounts.google.com/b/0/DisplayUnlockCaptcha"/> google link
	 * 2</a>
	 */
	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOGGER.info("Enviando de Email de confirmação");
		LOGGER.info(msg.toString());
		javaMailSender.send(msg);
		LOGGER.info("Email enviado");

	}

}
