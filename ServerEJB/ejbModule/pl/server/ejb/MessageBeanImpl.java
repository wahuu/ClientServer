package pl.server.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import pl.server.dto.MessageBuffor;
import pl.server.dto.TextMessageBuffor;

@Named
@Stateless
public class MessageBeanImpl {

	@PersistenceContext
	private EntityManager em;

	public void addMessage(MessageBuffor messageBuffor) {
		em.persist(messageBuffor);
	}

	public void delete(MessageBuffor messageBuffor) {
		messageBuffor = em.merge(messageBuffor);
		em.remove(messageBuffor);
	}

	public List<MessageBuffor> getAll() {
		List<MessageBuffor> resultList = em.createQuery("select t from MessageBuffor t", MessageBuffor.class)
				.getResultList();
		return resultList;
	}

	public void update(MessageBuffor messageBuffor) {
		em.merge(messageBuffor);
	}

	public List<MessageBuffor> getNewByIp(String ip) {
		TypedQuery<MessageBuffor> query = em.createQuery(
				"Select t from MessageBuffor t where t.ip = :ip and t.status = 'NEW'", MessageBuffor.class);
		query.setParameter("ip", ip);
		return query.getResultList();
	}

}
