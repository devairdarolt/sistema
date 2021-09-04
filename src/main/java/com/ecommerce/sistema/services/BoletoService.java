/**
 * Como o sistema não tem webservice para criação do boleto esta classe apenas
 * gera uma baixa fake com vencimento de 7 dias após o instante que o boleto foi
 * gerado
 * 
 * @author devairdarolt
 *
 */
package com.ecommerce.sistema.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.ecommerce.sistema.domain.PagamentoComBoleto;


@Service
public class BoletoService {

	/**
	 * Gera uma data de vencimento para 7 dias
	 * @param pagto
	 * @param instanteDoPedido
	 */
	public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instanteDoPedido) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(instanteDoPedido);
		cal.add(Calendar.DAY_OF_MONTH, 7);
		
		pagto.setDataVencimento(cal.getTime());
	}
}
