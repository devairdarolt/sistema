package com.ecomerce.sistema.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecomerce.sistema.domain.Categoria;
import com.ecomerce.sistema.domain.Produto;
import com.ecomerce.sistema.dto.CategoriaDTO;
import com.ecomerce.sistema.dto.ProdutoDTO;
import com.ecomerce.sistema.resources.utils.URL;
import com.ecomerce.sistema.services.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

	@Autowired
	ProdutoService service;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable Integer id) {
		Produto obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}

	/**
	 * 
	 * @param nome
	 * @param categorias
	 * @param page
	 * @param linesPerPage
	 * @param orderBy
	 * @param direction
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> findPage(/***/
			@RequestParam(value = "nome", defaultValue = "") String nome,
			@RequestParam(value = "categorias", defaultValue = "") String categorias,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		
		//Necessários pois o GET passa os parâmetros por URL ao invés do body
		List<Integer> ids = URL.decodeIntList(categorias);
		String nomeDecode = URL.decodeParam(nome);
		
		//Page<Produto> list = service.search(nomeDecode,ids,page, linesPerPage, orderBy, direction);
		Page<Produto> list = service.search(nomeDecode,ids,page, linesPerPage, orderBy, direction);
		
		Page<ProdutoDTO> listDto = list.map(obj -> new ProdutoDTO(obj)); // Page não precisa de stream()
		return ResponseEntity.ok().body(listDto);
		//throw new UnsupportedOperationException();
	}

}
