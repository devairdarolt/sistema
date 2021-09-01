package com.ecomerce.sistema.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ecomerce.sistema.domain.Cliente;
import com.ecomerce.sistema.dto.ClienteDTO;
import com.ecomerce.sistema.dto.ClienteNewDTO;
import com.ecomerce.sistema.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

	@Autowired
	ClienteService service;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable Integer id) {
		Cliente obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}

	/**
	 * INSERT
	 * 
	 * @param obj
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO objDto) {
		Cliente cliente = service.fromDTO(objDto);
		Cliente obj = service.insert(cliente);
		// Para retornar como resposta HTTP 201 a url com o Id ==
		// http://localhost:8080/categorias/{id}
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		// Cria o código HTTP 201 para informar que foi criado uma nova Cliente
		return ResponseEntity.created(uri).build();
	}

	/**
	 * UPDATE
	 * 
	 * @param id
	 * @param obj
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@PathVariable Integer id, @Valid @RequestBody ClienteDTO objDto) {
		Cliente obj = service.fromDTO(objDto);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}

	/**
	 * DELETE
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Lista todas as categorias passando o objeto Cliente para ClienteDTO na qual
	 * não são listados os produtos atribuídos a cada categoria
	 * 
	 * @return ResponseEntity<List<ClienteDTO>>
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ClienteDTO>> findAll() {
		List<Cliente> list = service.findAll();
		List<ClienteDTO> listDto = list.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<ClienteDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		Page<Cliente> list = service.findPage(page, linesPerPage, orderBy, direction);
		Page<ClienteDTO> listDto = list.map(obj -> new ClienteDTO(obj)); // Page não precisa de stream()
		return ResponseEntity.ok().body(listDto);
	}

}
