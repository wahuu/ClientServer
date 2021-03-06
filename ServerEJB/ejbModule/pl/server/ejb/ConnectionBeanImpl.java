package pl.server.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import pl.server.dto.Connections;

/**
 * 
 * Dodawanie,pobieranie, usuwanie do bazy danych 
 */
@Named
@Stateless
public class ConnectionBeanImpl {

	@PersistenceContext
	private EntityManager em;

	public void addConnection(Connections connection) {    
		em.persist(connection);
	}

	public void delete(Connections connection) {
		connection = em.merge(connection);
		em.remove(connection);
	}

	public List<Connections> getAll() {
		List<Connections> resultList = em.createQuery("select c from Connections c", Connections.class).getResultList();
		return resultList;
	}

	public void update(Connections connection) {
		em.merge(connection);
	}

	public List<Connections> getByIp(String ip) {
		TypedQuery<Connections> query = em.createQuery("Select c from Connections c where c.ip = :ip",
				Connections.class);
		query.setParameter("ip", ip);
		return query.getResultList();
	}

}
