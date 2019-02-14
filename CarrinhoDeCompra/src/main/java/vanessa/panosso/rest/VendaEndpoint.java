package vanessa.panosso.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
import vanessa.panosso.rest.dto.VendaDTO;
import vanessa.panosso.model.Venda;
import vanessa.panosso.model.VendaProduto;

/**
 * 
 */
@Stateless
@Path("/vendas")
public class VendaEndpoint {
	@PersistenceContext(unitName = "CarrinhoDeCompra-persistence-unit")
	private EntityManager em;

	@POST
	@Consumes("application/json")
	public Response create(VendaDTO dto) {

		Venda entity = dto.fromDTO(null, em);
		entity.getProdutos().forEach(p -> p.setVenda(entity));
		em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(VendaEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		Venda entity = em.find(Venda.class, id);
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
		TypedQuery<Venda> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT v FROM Venda v LEFT JOIN FETCH v.produtos WHERE v.id = :entityId ORDER BY v.id",
						Venda.class);
		findByIdQuery.setParameter("entityId", id);
		Venda entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		VendaDTO dto = new VendaDTO(entity);
		return Response.ok(dto).build();
	}

	@GET
	@Produces("application/json")
	public List<VendaDTO> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		TypedQuery<Venda> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT v FROM Venda v LEFT JOIN FETCH v.produtos ORDER BY v.id",
						Venda.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<Venda> searchResults = findAllQuery.getResultList();
		final List<VendaDTO> results = new ArrayList<VendaDTO>();
		for (Venda searchResult : searchResults) {
			VendaDTO dto = new VendaDTO(searchResult);
			results.add(dto);
		}
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, VendaDTO dto) {
		if (dto == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (id == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(dto.getId())) {
			return Response.status(Status.CONFLICT).entity(dto).build();
		}
		Venda entity = em.find(Venda.class, id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		entity = dto.fromDTO(entity, em);

		try {
			for (VendaProduto p: entity.getProdutos()) {
				p.setVenda(entity);
				entity = em.merge(entity);
			}
			
		} catch (OptimisticLockException e) {
			return Response.status(Status.CONFLICT).entity(e.getEntity())
					.build();
		}
		return Response.noContent().build();
	}
}
