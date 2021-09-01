package com.ecomerce.sistema.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.ecomerce.sistema.domain.Cliente;
import com.ecomerce.sistema.domain.enums.TipoCliente;
import com.ecomerce.sistema.dto.ClienteDTO;
import com.ecomerce.sistema.dto.ClienteNewDTO;
import com.ecomerce.sistema.repositories.ClienteRepository;
import com.ecomerce.sistema.resources.exceptions.FieldMessage;
import com.ecomerce.sistema.services.validation.utils.BR;

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
		Map<String,String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
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
}