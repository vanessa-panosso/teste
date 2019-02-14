package vanessa.panosso.rest.dto;

import java.io.Serializable;
import vanessa.panosso.model.Produto;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProdutoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String descricao;
	private String foto;
	private BigDecimal custoProduto;
	private byte[] arquivoFoto;

	public ProdutoDTO() {
	}

	public ProdutoDTO(final Produto entity) {
		if (entity != null) {
			this.id = entity.getId();
			this.nome = entity.getNome();
			this.descricao = entity.getDescricao();
			this.foto = entity.getFoto();
			this.custoProduto = entity.getCustoProduto();
		}
	}

	public Produto fromDTO(Produto entity, EntityManager em) {
		if (entity == null) {
			entity = new Produto();
		}
		entity.setNome(this.nome);
		entity.setDescricao(this.descricao);
		entity.setFoto(this.foto);
		entity.setCustoProduto(this.custoProduto);
		entity = em.merge(entity);
		return entity;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	public String getFoto() {
		return this.foto;
	}

	public void setFoto(final String foto) {
		this.foto = foto;
	}

	public BigDecimal getCustoProduto() {
		return this.custoProduto;
	}

	public void setCustoProduto(final BigDecimal custoProduto) {
		this.custoProduto = custoProduto;
	}

	public byte[] getArquivoFoto() {
		return arquivoFoto;
	}

	public void setArquivoFoto(byte[] arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}
}