package vanessa.panosso.rest.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.xml.bind.annotation.XmlRootElement;

import vanessa.panosso.model.Venda;
import vanessa.panosso.model.VendaProduto;

@XmlRootElement
public class VendaDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private BigDecimal valorVenda;
	private BigDecimal valorDespesasTotais;
	private Set<NestedVendaProdutoDTO> produtos = new HashSet<NestedVendaProdutoDTO>();

	public VendaDTO() {
	}

	public VendaDTO(final Venda entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.valorVenda = entity.getValorVenda();
			this.valorDespesasTotais = entity.getValorDespesasTotais();
			Iterator<VendaProduto> iterProdutos = entity.getProdutos()
					.iterator();
			while (iterProdutos.hasNext()) {
				VendaProduto element = iterProdutos.next();
				this.produtos.add(new NestedVendaProdutoDTO(element));
			}
		}
	}

	public Venda fromDTO(Venda entity, EntityManager em) {
		if (entity == null) {
			entity = new Venda();
		}
		entity.setValorVenda(this.valorVenda);
		entity.setValorDespesasTotais(this.valorDespesasTotais);

		for (NestedVendaProdutoDTO p: produtos) {
			VendaProduto vp = new VendaProduto();
			vp.setId(p.getId());
			vp.setMargemLucro(p.getMargemLucro());
			vp.setProduto(p.getProduto());
			vp.setQuantidade(p.getQuantidade());
			vp.setValor(p.getValor());
			vp.setValorCustoProduto(p.getValorCustoProduto());
			vp.setValorDespesa(p.getValorDespesa());
			vp.setVenda(entity);
			entity.getProdutos().add(vp);
		}
		entity = em.merge(entity);
		return entity;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public BigDecimal getValor_venda() {
		return this.valorVenda;
	}

	public void setValorVenda(final BigDecimal valorVenda) {
		this.valorVenda = valorVenda;
	}

	public Set<NestedVendaProdutoDTO> getProdutos() {
		return this.produtos;
	}

	public void setProdutos(final Set<NestedVendaProdutoDTO> produtos) {
		this.produtos = produtos;
	}

	public BigDecimal getValorDespesasTotais() {
		return valorDespesasTotais;
	}

	public void setValorDespesasTotais(BigDecimal valorDespesasTotais) {
		this.valorDespesasTotais = valorDespesasTotais;
	}
}