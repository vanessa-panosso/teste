package vanessa.panosso.rest;

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
import vanessa.panosso.model.VendaProduto;

/**
 * 
 */
@Stateless
@Path("/vendaprodutos")
public class VendaProdutoEndpoint {
	@PersistenceContext(unitName = "CarrinhoDeCompra-persistence-unit")
	private EntityManager em;

	@POST
	@Consumes("application/json")
	public Response create(VendaProduto entity) {
		em.persist(entity);

		return Response.created(
				UriBuilder.fromResource(VendaProdutoEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(@PathParam("id") Long id) {
		VendaProduto entity = em.find(VendaProduto.class, id);
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
		TypedQuery<VendaProduto> findByIdQuery = em
				.createQuery(
						"SELECT DISTINCT v FROM VendaProduto v LEFT JOIN FETCH v.produto LEFT JOIN FETCH v.venda WHERE v.id = :entityId ORDER BY v.id",
						VendaProduto.class);
		findByIdQuery.setParameter("entityId", id);
		VendaProduto entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
	}

	@GET
	@Produces("application/json")
	public List<VendaProduto> listAll(
			@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		TypedQuery<VendaProduto> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT v FROM VendaProduto v LEFT JOIN FETCH v.produto LEFT JOIN FETCH v.venda ORDER BY v.id",
						VendaProduto.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<VendaProduto> results = findAllQuery.getResultList();
		return results;
	}
	@GET
	@Produces("application/json")
	@Path("/{id:[0-9][0-9]*}")
	public List<VendaProduto> listAllByVenda(@PathParam("id") Long id,
			@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		TypedQuery<VendaProduto> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT v FROM VendaProduto v LEFT JOIN FETCH v.produto LEFT JOIN FETCH v.venda WHERE v.venda.id = :id ORDER BY v.id",
						VendaProduto.class);
		findAllQuery.setParameter("id", id);

		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		final List<VendaProduto> results = findAllQuery.getResultList();
		return results;
	}
	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, VendaProduto entity) {
		if (entity == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (id == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(entity.getId())) {
			return Response.status(Status.CONFLICT).entity(entity).build();
		}
		if (em.find(VendaProduto.class, id) == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		try {
			entity = em.merge(entity);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT)
					.entity(e.getEntity()).build();
		}

		return Response.noContent().build();
	}
}
