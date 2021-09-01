package com.ecomerce.sistema.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ecomerce.sistema.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

	@Transactional(readOnly = true) // Para fazer leituras rápidas do banco de dados sem Transaction Lock
	Cliente findByEmail(String email);
}
