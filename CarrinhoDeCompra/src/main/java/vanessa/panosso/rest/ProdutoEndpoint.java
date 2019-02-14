package vanessa.panosso.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import vanessa.panosso.model.Produto;
import vanessa.panosso.rest.dto.ProdutoDTO;
import vanessa.panosso.service.FileItem;
import vanessa.panosso.service.UploadImagem;

/**
 * 
 */
@Stateless
@Path("/produtos")
public class ProdutoEndpoint {
	
	@Inject
	private ServletContext context;
	
	@PersistenceContext(unitName = "CarrinhoDeCompra-persistence-unit")
	private EntityManager em;
	
	@POST
	@Consumes("application/json")
	public Response create(ProdutoDTO dto) throws Exception {
		Produto entity = dto.fromDTO(null, em);
		if (dto.getArquivoFoto() != null) {
			UploadImagem u = new UploadImagem(context);
			FileItem item = new FileItem();
			item.setFoto(dto.getArquivoFoto());
			item.setName(dto.getNome());
			entity.setFoto(u.inserImagemDiretorio(item));
		}
		
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(ProdutoEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		Produto entity = em.find(Produto.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		em.remove(entity);
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		TypedQuery<Produto> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT p FROM Produto p WHERE p.id = :entityId ORDER BY p.id",
						Produto.class);
		findByIdQuery.setParameter("entityId", id);
		Produto entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		ProdutoDTO dto = new ProdutoDTO(entity);
		return Response.ok(dto).build();
	}

	@GET
	@Produces("application/json")
	public List<ProdutoDTO> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) throws IOException {
		TypedQuery<Produto> findAllQuery = em
				.createQuery("SELECT DISTINCT p FROM Produto p ORDER BY p.id",
						Produto.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<Produto> searchResults = findAllQuery.getResultList();
		final List<ProdutoDTO> results = new ArrayList<ProdutoDTO>();
		for (Produto searchResult : searchResults) {
			ProdutoDTO dto = new ProdutoDTO(searchResult);
	    	final String PATH_ARQUIVOS = context.getRealPath("/img");
	    	File file = new File(PATH_ARQUIVOS + searchResult.getFoto());
	    	if (file.exists()) {
				dto.setArquivoFoto(Files.readAllBytes(file.toPath()));
	    	}
			results.add(dto);
		}
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, ProdutoDTO dto) {
		if (dto == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (id == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(dto.getId())) {
			return Response.status(Status.CONFLICT).entity(dto).build();
		}
		Produto entity = em.find(Produto.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		entity = dto.fromDTO(entity, em);
		try {
			entity = em.merge(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Status.CONFLICT).entity(e.getEntity())
					.build();
		}
		return Response.noContent().build();
	}
}
