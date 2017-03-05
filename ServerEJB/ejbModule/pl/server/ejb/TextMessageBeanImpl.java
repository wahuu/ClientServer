package pl.server.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import pl.server.dto.TextMessageBuffor;

@Named
@Stateless
public class TextMessageBeanImpl {

	@PersistenceContext
	private EntityManager em;

	public void addMessage(TextMessageBuffor textMessageBuffor) {
		em.persist(textMessageBuffor);
	}

	public void delete(TextMessageBuffor textMessageBuffor) {
		textMessageBuffor = em.merge(textMessageBuffor);
		em.remove(textMessageBuffor);
	}

	public List<TextMessageBuffor> getAll() {
		List<TextMessageBuffor> resultList = em
				.createQuery("select t from TextMessageBuffor t", TextMessageBuffor.class).getResultList();
		return resultList;
	}

	public void update(TextMessageBuffor textMessageBuffor) {
		em.merge(textMessageBuffor);
	}

	public List<TextMessageBuffor> getNewByIp(String ip) {
		TypedQuery<TextMessageBuffor> query = em.createQuery("Select t from TextMessageBuffor t where t.ip = :ip and t.status = 'NEW'",
				TextMessageBuffor.class);
		query.setParameter("ip", ip);
		return query.getResultList();
	}

}
