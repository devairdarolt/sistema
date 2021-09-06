package com.ecommerce.sistema.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.ecommerce.sistema.domain.Pedido;

public abstract class AbstractEmailService implements EmailService{
	@Value("${default.sender}")
	private String sender;
	
	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {
		SimpleMailMessage smm = prepareSimpleMailMessage(obj);
		sendEmail(smm);
	}

	protected SimpleMailMessage prepareSimpleMailMessage(Pedido obj) {
		SimpleMailMessage smm = new SimpleMailMessage();
		//destino
		smm.setTo(obj.getCliente().getEmail());
		//remetente
		smm.setFrom(this.sender);
		//assunto
		smm.setSubject("Pedido confirmado! código: "+obj.getId());
		smm.setSentDate(new Date(System.currentTimeMillis()));
		//Corpo do email
		smm.setText(obj.toString());
		return smm;
	}	
	
}
