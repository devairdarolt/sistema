package com.ecommerce.sistema.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.sistema.domain.ItemPedido;
import com.ecommerce.sistema.domain.Pagamento;
import com.ecommerce.sistema.domain.PagamentoComBoleto;
import com.ecommerce.sistema.domain.Pedido;
import com.ecommerce.sistema.domain.enums.EstadoPagamento;
import com.ecommerce.sistema.repositories.ItemPedidoRepository;
import com.ecommerce.sistema.repositories.PagamentoRepository;
import com.ecommerce.sistema.repositories.PedidoRepository;
import com.ecommerce.sistema.repositories.ProdutoRepository;
import com.ecommerce.sistema.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	@Autowired
	private BoletoService boletoService;
	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private ProdutoService produtoService;

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);// Garantia de criação de um novo objeto no banco
		obj.setInstante(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);// O pagamento precisa ter referência ao Pedido
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		
		//Persist Pagamento
		Pagamento pagamento = pagamentoRepository.save(obj.getPagamento());

		// Agora falta associar ItemPedido ao Pedido e Pagamento
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);

			// ip.setPreco(produtoRepository.findById(ip.getProduto().getId()).get().getPreco());
			ip.setPreco(produtoService.find(ip.getProduto().getId()).getPreco());
			ip.setPedido(obj);
		}
		
		//Persist itens
		itemPedidoRepository.saveAll(obj.getItens());

		return obj;
	}
}