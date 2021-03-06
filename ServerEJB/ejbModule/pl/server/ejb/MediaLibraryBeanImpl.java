package pl.server.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import pl.server.dto.MediaLibrary;
/**
 * 
 * operacje na tabeli MediaLibrary
 */
@Named
@Stateless
public class MediaLibraryBeanImpl {

	@PersistenceContext
	private EntityManager em;

	public void add(MediaLibrary mediaLibrary) {
		em.persist(mediaLibrary);
	}

	public List<MediaLibrary> getAll() {
		List<MediaLibrary> resultList = em.createQuery("select t from MediaLibrary t", MediaLibrary.class)
				.getResultList();
		return resultList;
	}

	public MediaLibrary getbyId(int id) {
		MediaLibrary result = em.find(MediaLibrary.class, id);
		return result;
	}

	public List<MediaLibrary> getByName(String name) {
		TypedQuery<MediaLibrary> query = em.createQuery("select t from MediaLibrary t where t.name = :name",
				MediaLibrary.class);
		query.setParameter("name", name);
		return query.getResultList();
	}

}
