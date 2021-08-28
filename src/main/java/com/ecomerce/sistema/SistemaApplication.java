package com.ecomerce.sistema;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ecomerce.sistema.domain.Categoria;
import com.ecomerce.sistema.repositories.CategoriaRepository;

@SpringBootApplication
public class SistemaApplication implements CommandLineRunner{
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(SistemaApplication.class, args);
	}

	/**
	 * Instancias do modelo conceitual {@link #2}
	 */
	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");

		categoriaRepository.saveAll(Arrays.asList(cat1,cat2));
		
	}

}
