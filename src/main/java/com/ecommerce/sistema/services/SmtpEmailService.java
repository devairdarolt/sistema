package com.ecommerce.sistema.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SmtpEmailService extends AbstractEmailService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SmtpEmailService.class);
	@Autowired
	private MailSender mailSender;

	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOGGER.info("Enviando de Email de confirmação");
		LOGGER.info(msg.toString());
		// Liberar app menos seguro em:
		// https://myaccount.google.com/lesssecureapps?pli=1&rapt=AEjHL4PsS8rMpphE8PeG7TelQ5D0f7qr4Ka0wrcdeaeaYqGHH0yZ_3mCYCOeanGWODTb7g1GKx0Dlb4AOgtCOPVxMZ-Q5GoRIw
		mailSender.send(msg);
		LOGGER.info("Email enviado");

	}

}
