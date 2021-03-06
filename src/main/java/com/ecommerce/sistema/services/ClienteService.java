package com.ecommerce.sistema.services;

import java.util.List;
import java.util.Optional;

import com.ecommerce.sistema.domain.Cidade;
import com.ecommerce.sistema.domain.Cliente;
import com.ecommerce.sistema.domain.Endereco;
import com.ecommerce.sistema.domain.enums.TipoCliente;
import com.ecommerce.sistema.dto.ClienteDTO;
import com.ecommerce.sistema.dto.ClienteNewDTO;
import com.ecommerce.sistema.repositories.CidadeRepository;
import com.ecommerce.sistema.repositories.ClienteRepository;
import com.ecommerce.sistema.repositories.EnderecoRepository;
import com.ecommerce.sistema.services.exceptions.DataIntegrityException;
import com.ecommerce.sistema.services.exceptions.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	@Autowired
	private CidadeRepository repoCidade;
	@Autowired
	private EnderecoRepository repoEndereco;

	@Autowired
	private BCryptPasswordEncoder passEncoder;

	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
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
			throw new DataIntegrityException("Não é possível excluir um Cliente que possui pedodos relacionados");
		}

	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);

	}

	public Cliente fromDTO(ClienteDTO objDto) {
		Cliente cliente = new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
		return cliente;
		// throw new UnsupportedOperationException();
	}

	public Cliente fromDTO(ClienteNewDTO objDto) {

		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(),
				TipoCliente.toEnum(objDto.getTipo()), passEncoder.encode(objDto.getSenha()));
		
		Optional<Cidade> optCi = repoCidade.findById(objDto.getCidadeId());
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(),
				objDto.getBairro(), objDto.getCep(), cli, optCi.get());
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if (objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if (objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}

		return cli;
	}

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		repoEndereco.saveAll(obj.getEnderecos());
		return obj;
	}
}
