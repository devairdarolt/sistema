package com.ecommerce.sistema.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecommerce.sistema.domain.Cliente;
import com.ecommerce.sistema.repositories.ClienteRepository;
import com.ecommerce.sistema.security.UserSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private ClienteRepository clienteRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Cliente cli = clienteRepo.findByEmail(email);
		if(cli==null) {
			throw new UsernameNotFoundException(email);
		}		
		return new UserSS(cli.getId(),cli.getEmail(),cli.getSenha(),cli.getPerfis());
	}

}
