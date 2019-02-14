package vanessa.panosso.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import vanessa.panosso.model.Venda;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "venda_produto")
@XmlRootElement
public class VendaProduto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(nullable = false)
	private Long quantidade;

	@Column(nullable = false, name = "valor_despesa")
	private BigDecimal valorDespesa;

	@Column(nullable = false)
	private BigDecimal valor;

	@Column(nullable = false, name = "valor_custo_produto")
	private BigDecimal valorCustoProduto;

	@Column(name = "margem_lucro")
	private BigDecimal margemLucro;

	@OneToOne
	private Produto produto;
	
	@ManyToOne
	private Venda venda;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof VendaProduto)) {
			return false;
		}
		VendaProduto other = (VendaProduto) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorDespesa() {
		return valorDespesa;
	}

	public void setValorDespesa(BigDecimal valorDespesa) {
		this.valorDespesa = valorDespesa;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getValorCustoProduto() {
		return valorCustoProduto;
	}

	public void setValorCustoProduto(BigDecimal valorCustoProduto) {
		this.valorCustoProduto = valorCustoProduto;
	}

	public BigDecimal getMargemLucro() {
		return margemLucro;
	}

	public void setMargemLucro(BigDecimal margemLucro) {
		this.margemLucro = margemLucro;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (quantidade != null)
			result += "quantidade: " + quantidade;
		return result;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}
}