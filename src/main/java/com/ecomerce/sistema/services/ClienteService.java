package com.ecomerce.sistema.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.ecomerce.sistema.domain.Cliente;
import com.ecomerce.sistema.domain.Cliente;
import com.ecomerce.sistema.dto.ClienteDTO;
import com.ecomerce.sistema.repositories.ClienteRepository;
import com.ecomerce.sistema.services.exceptions.DataIntegrityException;
import com.ecomerce.sistema.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				 "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}


	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj,obj);
		return repo.save(newObj);
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		
	}


	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um Cliente que possui entidades relacionadas");
		}

	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page,linesPerPage , Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
		
	}
	public Cliente fromDTO(ClienteDTO objDto) {
		Cliente cliente = new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
		return cliente;
		//throw new UnsupportedOperationException();
	}
}
