package com.ecomerce.sistema;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ecomerce.sistema.domain.Categoria;
import com.ecomerce.sistema.domain.Cidade;
import com.ecomerce.sistema.domain.Cliente;
import com.ecomerce.sistema.domain.Endereco;
import com.ecomerce.sistema.domain.Estado;
import com.ecomerce.sistema.domain.ItemPedido;
import com.ecomerce.sistema.domain.Pagamento;
import com.ecomerce.sistema.domain.PagamentoComBoleto;
import com.ecomerce.sistema.domain.PagamentoComCartao;
import com.ecomerce.sistema.domain.Pedido;
import com.ecomerce.sistema.domain.Produto;
import com.ecomerce.sistema.domain.enums.EstadoPagamento;
import com.ecomerce.sistema.domain.enums.TipoCliente;
import com.ecomerce.sistema.repositories.CategoriaRepository;
import com.ecomerce.sistema.repositories.CidadeRepository;
import com.ecomerce.sistema.repositories.ClienteRepository;
import com.ecomerce.sistema.repositories.EnderecoRepository;
import com.ecomerce.sistema.repositories.EstadoRepository;
import com.ecomerce.sistema.repositories.ItemPedidoRepository;
import com.ecomerce.sistema.repositories.PagamentoRepository;
import com.ecomerce.sistema.repositories.PedidoRepository;
import com.ecomerce.sistema.repositories.ProdutoRepository;

@SpringBootApplication
public class SistemaApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	ItemPedidoRepository itemPedidoRepository;

	public static void main(String[] args) {
		SpringApplication.run(SistemaApplication.class, args);
	}

	/**
	 * Instancias do modelo conceitual {@link #2}
	 */
	@Override
	public void run(String... args) throws Exception {

		/**
		 * CATEGORIA, PRODUTO
		 */
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");
		Categoria cat3 = new Categoria(null, "Games");
		Categoria cat4 = new Categoria(null, "Festas");
		Categoria cat5 = new Categoria(null, "Eletrônicos");
		Categoria cat6 = new Categoria(null, "Softwares");
		Categoria cat7 = new Categoria(null, "Veículos");

		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);

		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().add(p2);

		p1.getCategorias().add(cat1);
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().add(cat1);

		categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5, cat6, cat7));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));

		/**
		 * CIDADE, ESTADO
		 */

		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");

		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);

		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2, c3));

		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));

		/**
		 * CLIENTE, TELEFONES, ENDEREÇOS
		 */

		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "36378912377", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("27363323", "93838393"));

		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 203", "Bairo Jardim", "38220834", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38777012", cli1, c2);

		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));

		clienteRepository.save(cli1);
		enderecoRepository.saveAll(Arrays.asList(e1, e2));

		/**
		 * PEDIDO, PAGAMENTO, ESTADO PAGAMENTO,
		 */

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);// Removido pagamento do construtor do
																				// Pedido para poder instanciar um
																				// idependente do outro
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"),
				null);
		ped2.setPagamento(pagto2);
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));

		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));
		clienteRepository.save(cli1); // NÃO É NECESSÁRIO

		/**
		 * ITEM_PEDIDO
		 */
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, p1.getPreco());
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, p3.getPreco());
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, p2.getPreco());

		ped1.getItens().addAll(Arrays.asList(ip1, ip2));
		ped2.getItens().add(ip3);

		p1.getItens().add(ip1);
		p2.getItens().addAll(Arrays.asList(ip3));
		p3.getItens().addAll(Arrays.asList(ip2));
		itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));

	}

}
