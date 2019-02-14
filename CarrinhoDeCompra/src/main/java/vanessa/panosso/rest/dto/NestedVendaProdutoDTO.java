package vanessa.panosso.rest.dto;

import java.io.Serializable;

import vanessa.panosso.model.Produto;
import vanessa.panosso.model.VendaProduto;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;

public class NestedVendaProdutoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long quantidade;
	private BigDecimal valorDespesa;
	private BigDecimal valor;
	private BigDecimal valorCustoProduto;
	private BigDecimal margemLucro;
	private Produto produto;

	public NestedVendaProdutoDTO() {
	}

	public NestedVendaProdutoDTO(final VendaProduto entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.quantidade = entity.getQuantidade();
			this.valorDespesa = entity.getValorDespesa();
			this.valor = entity.getValor();
			this.valorCustoProduto = entity.getValorCustoProduto();
			this.margemLucro = entity.getMargemLucro();
			this.setProduto(entity.getProduto());
		}
	}

	public VendaProduto fromDTO(VendaProduto entity, EntityManager em) {
		if (entity == null) {
			entity = new VendaProduto();
		}
		if (this.id != null) {
			TypedQuery<VendaProduto> findByIdQuery = em
					.createQuery(
							"SELECT DISTINCT v FROM VendaProduto v WHERE v.id = :entityId",
							VendaProduto.class);
			findByIdQuery.setParameter("entityId", this.id);
			try {
				entity = findByIdQuery.getSingleResult();
			} catch (javax.persistence.NoResultException nre) {
				entity = null;
			}
			return entity;
		}
		entity.setQuantidade(this.quantidade);
		entity.setValorDespesa(this.valorDespesa);
		entity.setValor(this.valor);
		entity.setValorCustoProduto(this.valorCustoProduto);
		entity.setMargemLucro(this.margemLucro);
		entity.setProduto(this.getProduto());
		entity = em.merge(entity);
		return entity;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Long getQuantidade() {
		return this.quantidade;
	}

	public void setQuantidade(final Long quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorDespesa() {
		return this.valorDespesa;
	}

	public void setValorDespesa(final BigDecimal valorDespesa) {
		this.valorDespesa = valorDespesa;
	}

	public BigDecimal getValor() {
		return this.valor;
	}

	public void setValor(final BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getValorCustoProduto() {
		return this.valorCustoProduto;
	}

	public void setValorCustoProduto(final BigDecimal valorCustoProduto) {
		this.valorCustoProduto = valorCustoProduto;
	}

	public BigDecimal getMargemLucro() {
		return this.margemLucro;
	}

	public void setMargemLucro(final BigDecimal margemLucro) {
		this.margemLucro = margemLucro;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
}