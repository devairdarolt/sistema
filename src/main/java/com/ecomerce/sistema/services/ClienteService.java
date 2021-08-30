package com.ecomerce.sistema.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomerce.sistema.domain.Cliente;
import com.ecomerce.sistema.repositories.ClienteRepository;
import com.ecomerce.sistema.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	public Cliente buscar(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				 "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
}
