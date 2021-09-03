package com.ecommerce.sistema.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.sistema.domain.Pagamento;

//Não precisa criar Repository de subclasses Pagamento
@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer>{

}
