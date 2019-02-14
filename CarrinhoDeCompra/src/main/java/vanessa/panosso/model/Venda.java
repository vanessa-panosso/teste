package vanessa.panosso.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "venda")
@XmlRootElement
public class Venda implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private BigDecimal valorVenda;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="venda", targetEntity=VendaProduto.class, fetch=FetchType.EAGER)
	private Set<VendaProduto> produtos = new HashSet<VendaProduto>();

	private BigDecimal valorDespesasTotais;

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
		if (!(obj instanceof Venda)) {
			return false;
		}
		Venda other = (Venda) obj;
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

	public BigDecimal getValorVenda() {
		return valorVenda;
	}

	public void setValorVenda(BigDecimal valorVenda) {
		this.valorVenda = valorVenda;
	}

	public Set<VendaProduto> getProdutos() {
		return this.produtos;
	}

	public void setProdutos(final Set<VendaProduto> produtos) {
		this.produtos = produtos;
	}

	public BigDecimal getValorDespesasTotais() {
		return valorDespesasTotais;
	}

	public void setValorDespesasTotais(BigDecimal valorDespesasTotais) {
		this.valorDespesasTotais = valorDespesasTotais;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (id != null)
			result += "id: " + id;
		if (valorVenda != null)
			result += ", valor_venda: " + valorVenda;
		if (produtos != null)
			result += ", produtos: " + produtos;
		if (valorDespesasTotais != null)
			result += ", valorDespesasTotais: " + valorDespesasTotais;
		return result;
	}

}