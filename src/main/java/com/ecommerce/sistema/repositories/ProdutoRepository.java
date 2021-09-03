package com.ecommerce.sistema.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.sistema.domain.Categoria;
import com.ecommerce.sistema.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

	/**
	 * Primeira forma de implementar utilizando JPQL
	 * @param nome
	 * @param categorias
	 * @param pageRequest
	 * @return
	 */
	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	Page<Produto> search(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias,
			Pageable pageRequest);
	
	/**
	 * Segunda forma de implemntar a mesma consulta
	 * @param nome
	 * @param categorias
	 * @param pageRequest
	 * @return
	 */
	
	@Transactional(readOnly = true) // Para fazer leituras rápidas do banco de dados sem Transaction Lock
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
}
