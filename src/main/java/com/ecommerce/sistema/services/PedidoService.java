package com.ecommerce.sistema.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.sistema.domain.ItemPedido;
import com.ecommerce.sistema.domain.PagamentoComBoleto;
import com.ecommerce.sistema.domain.Pedido;
import com.ecommerce.sistema.domain.enums.EstadoPagamento;
import com.ecommerce.sistema.repositories.ItemPedidoRepository;
import com.ecommerce.sistema.repositories.PagamentoRepository;
import com.ecommerce.sistema.repositories.PedidoRepository;
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
	private ItemPedidoRepository itemPedidoRepository;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private EmailService emailService;

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
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);// O pagamento precisa ter referência ao Pedido
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);

		// Persist Pagamento
		this.pagamentoRepository.save(obj.getPagamento());

		// Agora falta associar ItemPedido ao Pedido e Pagamento
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}

		// Persist itens
		itemPedidoRepository.saveAll(obj.getItens());		
		emailService.sendOrderConfirmationHtmlEmail(obj);
		
		
		return obj;
	}
}
