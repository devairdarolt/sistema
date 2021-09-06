package com.ecommerce.sistema.services;

import org.springframework.mail.SimpleMailMessage;

import com.ecommerce.sistema.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
}
