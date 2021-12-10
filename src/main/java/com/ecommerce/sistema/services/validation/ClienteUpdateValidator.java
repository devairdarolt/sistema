package com.ecommerce.sistema.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.ecommerce.sistema.domain.Cliente;
import com.ecommerce.sistema.dto.ClienteDTO;
import com.ecommerce.sistema.repositories.ClienteRepository;
import com.ecommerce.sistema.resources.exceptions.FieldMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

	@Autowired
	private HttpServletRequest request; // Necessário para pegar o Id do DTO pela URI

	@Autowired
	ClienteRepository repoCliente;

	@Override
	public void initialize(ClienteUpdate ann) {
	}

	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		Cliente clienteByEmail = repoCliente.findByEmail(objDto.getEmail());
		
		//Pegar Id de objDto pela URI
		Map<String,String> map = getIdFromUriRequest();
		Integer id = Integer.parseInt(map.get("id"));
		
		//Verificar se o email ja esta cadastrado para outro id
		if (clienteByEmail != null && !clienteByEmail.getId().equals(id)) {
			list.add(new FieldMessage("email", "Este E-mail já está vinculado a outro Cliente"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> getIdFromUriRequest() {
		return (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
	}
}